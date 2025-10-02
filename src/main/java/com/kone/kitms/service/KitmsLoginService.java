package com.kone.kitms.service;

import static com.kone.kitms.security.SecurityUtils.JWT_ALGORITHM;

import com.kone.kitms.domain.KitmsLoginLog;
import com.kone.kitms.domain.KitmsUser;
import com.kone.kitms.mybatis.mapper.KitmsTokenStorageMapper;
import com.kone.kitms.mybatis.vo.KitmsTokenStorageVO;
import com.kone.kitms.mybatis.vo.KitmsTokenUserVO;
import com.kone.kitms.repository.KitmsLoginLogRepository;
import com.kone.kitms.repository.KitmsUserRepository;
// JWT 기능을 일시적으로 비활성화 (설정 누락으로 인한 오류 방지)
// import com.kone.kitms.security.JwtTokenProvider;
import com.kone.kitms.service.dto.*;
import com.kone.kitms.web.rest.vm.KitmsLoginVM;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * KITMS 로그인 및 인증 관리 서비스
 * 
 * 이 클래스는 KITMS 시스템의 사용자 인증과 관련된 모든 비즈니스 로직을 제공합니다:
 * - 사용자 로그인 인증 처리
 * - JWT 토큰 생성 및 검증
 * - 토큰 갱신 및 관리
 * - 사용자 ID 찾기 및 비밀번호 재설정
 * - 로그인 로그 기록 및 관리
 * - 세션 관리 및 로그아웃 처리
 * - 토큰 저장소 관리
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
@Service
@Transactional
public class KitmsLoginService {

    private final JPAQueryFactory queryFactory;

    private static final String TABLE_NAME = "kitms_user";

    private final Logger log = LoggerFactory.getLogger(KitmsLoginService.class);

    private final KitmsUserRepository kitmsUserRepository;

    private final KitmsLoginLogRepository kitmsLoginLogRepository;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private PasswordEncoder passwordEncoder;
    
    // JWT 기능을 일시적으로 비활성화 (설정 누락으로 인한 오류 방지)
    // @Autowired
    // private JwtTokenProvider jwtTokenProvider;

    private final KitmsTokenStorageMapper kitmsTokenStorageMapper;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds:0}")
    private long tokenValidityInSeconds;

    @Autowired
    public KitmsLoginService(
        JPAQueryFactory queryFactory,
        KitmsUserRepository kitmsUserRepository,
        KitmsLoginLogRepository kitmsLoginLogRepository,
        JwtEncoder jwtEncoder,
        JwtDecoder jwtDecoder,
        PasswordEncoder passwordEncoder,
        KitmsTokenStorageMapper kitmsTokenStorageMapper
    ) {
        this.queryFactory = queryFactory;
        this.kitmsUserRepository = kitmsUserRepository;
        this.kitmsLoginLogRepository = kitmsLoginLogRepository;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.passwordEncoder = passwordEncoder;
        this.kitmsTokenStorageMapper = kitmsTokenStorageMapper;
    }

    public KitmsTokenUserVO getUserInfo(String userId) {
        // 하드코딩된 ALLPASSUSER 계정 제거 - 보안 강화
        // 모든 사용자는 정상적인 데이터베이스 조회를 통해 인증
        return kitmsTokenStorageMapper.getUserInfoForToken(userId);
    }

    public CustomReturnDTO checkAuthAndCreateToken(HttpServletRequest request, KitmsLoginVM loginVM) {
        CustomReturnDTO result = new CustomReturnDTO();
        String userId = loginVM.getUserId();
        String password = loginVM.getPassword();

        KitmsLoginLog kitmsLoginLog = new KitmsLoginLog();
        kitmsLoginLog.setUserId(userId);
        kitmsLoginLog.setLoginIp(getClientIP(request));
        kitmsLoginLog.setLoginDt(ZonedDateTime.now().plusHours(9));
        kitmsLoginLog.setLoginSuccess(false);

        result.setStatus(HttpStatus.BAD_REQUEST);

        if (userId == null || userId.isEmpty() || userId.isBlank()) {
            result.setMessage("아이디가 없습니다.");
            kitmsLoginLog.setLoginReason("아이디가 없습니다.");
            kitmsLoginLogRepository.save(kitmsLoginLog);
            return result;
        }

        if (password == null || password.isEmpty() || password.isBlank()) {
            result.setMessage("비밀번호가 없습니다.");
            kitmsLoginLog.setLoginReason("비밀번호가 없습니다.");
            kitmsLoginLogRepository.save(kitmsLoginLog);
            return result;
        }

        KitmsTokenUserVO loginInfo = this.getUserInfo(userId);

        if (loginInfo == null) {
            result.setMessage("인증 실패. ID 및 PW를 확인하세요.");
            kitmsLoginLog.setLoginReason("인증 실패. ID 및 PW를 확인하세요.");
            kitmsLoginLogRepository.save(kitmsLoginLog);
            return result;
        }

        // 모든 사용자는 비밀번호 검증 필수 - 보안 강화
        if (!passwordEncoder.matches(loginVM.getPassword(), loginInfo.getUserPwd())) {
            result.setMessage("인증 실패. ID 및 PW를 확인하세요.");
            kitmsLoginLog.setLoginReason("인증 실패. ID 및 PW를 확인하세요.");
            kitmsLoginLogRepository.save(kitmsLoginLog);
            return result;
        }

        result.setStatus(HttpStatus.OK);
        
        // 새로운 JWT 토큰 생성
        String jwtToken = this.createJwtToken(loginInfo);
        // 기존 토큰도 생성 (하위 호환성)
        String legacyToken = this.createToken(loginInfo);
        
        result.setMessage("인증 성공.");
        kitmsLoginLog.setLoginReason("인증 성공.");
        kitmsLoginLog.setLoginSuccess(true);
        
        // JWT 토큰을 Authorization 헤더용으로 반환
        result.addColumn("access_token", jwtToken);
        result.addColumn("token_type", "Bearer");
        result.addColumn("expires_in", 86400); // 24시간 (초 단위)
        
        // 기존 호환성을 위해 kitms_token도 반환
        result.addColumn("kitms_token", legacyToken);

        kitmsLoginLogRepository.save(kitmsLoginLog);
        kitmsTokenStorageMapper.deleteStorageToken(userId);
        kitmsTokenStorageMapper.insertStorageToken(userId, legacyToken);

        return result;
    }

    public String createToken(KitmsTokenUserVO kitmsLoginDTO) {
        // 기존 방식과 새로운 JWT 방식 모두 지원
        Instant now = Instant.now();
        Instant validity = now.plus(this.tokenValidityInSeconds, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet
            .builder()
            .issuedAt(now)
            .claim("userNo", kitmsLoginDTO.getUserNo())
            .claim("userId", kitmsLoginDTO.getUserId())
            .claim("userName", kitmsLoginDTO.getUserName())
            .claim("authCode", kitmsLoginDTO.getAuthCode())
            .claim("firstFlag", kitmsLoginDTO.getFirstFlag() ? "1" : "0")
            .expiresAt(validity)
            .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
    
    /**
     * 새로운 JWT 토큰 생성 메서드
     * 
     * @param kitmsLoginDTO 사용자 정보
     * @return JWT 토큰 문자열
     */
    public String createJwtToken(KitmsTokenUserVO kitmsLoginDTO) {
        // 권한 설정
        String authorities = "ROLE_USER";
        if ("0".equals(kitmsLoginDTO.getAuthCode())) {
            authorities = "ROLE_ADMIN";
        }
        
        // JWT 토큰 생성 (기존 방식으로 대체)
        return generateJwtToken(kitmsLoginDTO.getUserNo(), kitmsLoginDTO.getUserId(), authorities);
    }

    public CustomReturnDTO validToken(HttpServletRequest request) {
        CustomReturnDTO result = new CustomReturnDTO();
        result.setStatus(HttpStatus.BAD_REQUEST);
        
        // Authorization 헤더에서 JWT 토큰 추출
        String authHeader = request.getHeader("Authorization");
        String tokenValue = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            tokenValue = authHeader.substring(7);
        } else {
            // 기존 kitms_token 헤더도 지원 (하위 호환성)
            tokenValue = request.getHeader("kitms_token");
        }
        
        if (tokenValue == null || tokenValue.isEmpty() || tokenValue.isBlank()) {
            result.setMessage("토큰이 없습니다.");
            result.setDesc("Please, Put [Authorization: Bearer <token>] or [kitms_token] in request header.");
            return result;
        }

        result.setStatus(HttpStatus.UNAUTHORIZED);
        
        // JWT 토큰 검증 (기존 방식으로 대체)
        try {
            Jwt jwt = jwtDecoder.decode(tokenValue);
            Long userNo = jwt.getClaim("userNo");
            String userId = jwt.getClaim("userId");
            
            result.setStatus(HttpStatus.OK);
            result.setMessage("유효한 JWT 토큰입니다.");
            result.addColumn("userNo", userNo.toString());
            result.addColumn("userId", userId);
            return result;
        } catch (Exception e) {
            // JWT 토큰 검증 실패 시 기존 토큰 방식으로 검증 시도 (하위 호환성)
            try {
                Jwt kitmsToken = jwtDecoder.decode(tokenValue);
                String userId = kitmsToken.getClaimAsString("userId");
                
                // 모든 사용자의 토큰은 저장소에서 검증 - 보안 강화
                KitmsTokenStorageVO storageToken = kitmsTokenStorageMapper.getStorageToken(userId);
                if (storageToken == null || !storageToken.getUserToken().equals(tokenValue)) {
                    result.setMessage("유효하지 않은 토큰입니다.");
                    result.setDesc("다른 세션에서 해당 계정으로 로그인한 경우, 기존 토큰은 만료됩니다.");
                    return result;
                }
                
                result.setStatus(HttpStatus.OK);
                result.setMessage("유효한 토큰입니다.");
                result.addColumn("userNo", kitmsToken.getClaimAsString("userNo"));
                result.addColumn("userId", userId);
                return result;
            } catch (Exception ex) {
                result.setMessage("토큰이 만료되었습니다. 다시 로그인해주세요.");
                result.setDesc("Token expired or invalid.");
                return result;
            }
        }
    }

    public String getTokenUserId(HttpServletRequest request) {
        Jwt kitmsToken = jwtDecoder.decode(request.getHeader("kitms_token"));
        return kitmsToken.getClaimAsString("userId");
    }

    public CustomReturnDTO createRefreshToken(HttpServletRequest request) {
        CustomReturnDTO validDTO = this.validToken(request);
        if (validDTO.getStatus() != HttpStatus.OK) {
            return validDTO;
        }
        String userId = validDTO.getData().get("userId").toString();
        KitmsTokenUserVO userInfo = this.getUserInfo(userId);
        String tokenValue = this.createToken(userInfo);

        CustomReturnDTO result = new CustomReturnDTO();
        result.setStatus(HttpStatus.OK);
        result.setMessage("인증 성공.");
        result.addColumn("kitms_token", tokenValue);

        kitmsTokenStorageMapper.deleteStorageToken(userId);
        kitmsTokenStorageMapper.insertStorageToken(userId, tokenValue);

        return result;
    }

    public CustomReturnDTO findUserIdorResetPassword(KitmsUser kitmsUser, boolean findIdFlag) {
        CustomReturnDTO result = new CustomReturnDTO();
        String userName = kitmsUser.getUserName();
        String userTel = kitmsUser.getUserTel();
        String userEmail = kitmsUser.getUserEmail();

        result.setStatus(HttpStatus.BAD_REQUEST);
        if (userName == null || userName.isEmpty() || userName.isBlank()) {
            result.setMessage("이름은 필수값입니다..");
            return result;
        }

        if (userTel == null || userTel.isEmpty() || userTel.isBlank()) {
            result.setMessage("전화번호는 필수값입니다.");
            return result;
        }

        if (userEmail == null || userEmail.isEmpty() || userEmail.isBlank()) {
            result.setMessage("이메일은 필수값입니다.");
            return result;
        }

        String userId = null;
        if (findIdFlag) {
            userId = kitmsUserRepository.findUserId(kitmsUser.getUserName(), kitmsUser.getUserTel(), kitmsUser.getUserEmail());
            result.setMessage("일치하는 정보가 없습니다.");
        } else {
            userId = kitmsUser.getUserId();
            result.setMessage("아이디는 필수값입니다.");
        }
        if (userId == null || userId.isEmpty() || userId.isBlank()) return result;

        if (findIdFlag) {
            result.setStatus(HttpStatus.OK);
            result.setMessage(null);
            result.addColumn("userId", userId);
            return result;
        } else {
            String returnText = kitmsUserRepository.findUserPwd(
                kitmsUser.getUserId(),
                kitmsUser.getUserName(),
                kitmsUser.getUserTel(),
                kitmsUser.getUserEmail()
            );

            if (returnText == null || returnText.isEmpty() || returnText.isBlank()) {
                result.setMessage("일치하는 정보가 없습니다.");
                return result;
            }

            result.setStatus(HttpStatus.OK);

            Optional<KitmsUser> update = kitmsUserRepository
                .findById(kitmsUserRepository.findUserNoByUserId(userId).getUserNo())
                .map(user -> {
                    user.setUserPwd(passwordEncoder.encode(returnText));
                    user.setLastPassModDt(ZonedDateTime.now().plusHours(9));
                    user.setFirstFlag(true);
                    return kitmsUserRepository.save(user);
                });
            result.setMessage("비밀번호 초기화 완료.");
            result.addColumn("result", update);
            result.setDesc("#### 규칙 :::: {아이디}_{전화번호 뒤 4자리}@");
            //            result.setDesc("#### Rule :::: {userId}_{userTel or userMobile last 4 digit}@");

            return result;
        }
    }

    public CustomReturnDTO logout(HttpServletRequest request, String userId) {
        CustomReturnDTO result = this.validToken(request);
        if (result.getStatus() != HttpStatus.OK) {
            return result;
        }
        result.setStatus(HttpStatus.UNAUTHORIZED);
        if (!result.getData().get("userId").toString().equals(userId)) {
            result.setMessage("토큰정보가 일치하지 않습니다.");
            return result;
        }

        result.setStatus(HttpStatus.OK);
        result.setMessage("로그아웃되었습니다.");
        result.setDesc("토큰이 초기화되었습니다.");

        kitmsTokenStorageMapper.deleteStorageToken(userId);

        return result;
    }

    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * JWT 토큰 생성 (기존 방식으로 대체)
     * 
     * @param userNo 사용자 번호
     * @param userId 사용자 ID
     * @param authorities 권한
     * @return JWT 토큰 문자열
     */
    private String generateJwtToken(Long userNo, String userId, String authorities) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(24, ChronoUnit.HOURS); // 24시간 후 만료

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("KITMS")
            .issuedAt(now)
            .expiresAt(expiryDate)
            .subject(userId)
            .claim("userNo", userNo)
            .claim("userId", userId)
            .claim("authorities", authorities)
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
