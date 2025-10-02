package com.kone.kitms.service;

import com.kone.kitms.domain.KitmsUser;
import com.kone.kitms.repository.KitmsUserRepository;
import com.kone.kitms.service.dto.CustomReturnDTO;
import com.kone.kitms.service.dto.KitmsUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * KITMS 사용자 관리 서비스
 * 
 * 시스템 사용자의 CRUD 작업 및 사용자 관련 비즈니스 로직을 처리하는 서비스입니다.
 * 
 * 주요 기능:
 * - 사용자 목록 조회 및 페이징 처리
 * - 사용자 생성, 수정, 삭제
 * - 사용자 비밀번호 암호화 및 검증
 * - 사용자 권한 관리
 * - 사용자 계정 활성화/비활성화
 * 
 * @author KITMS Development Team
 * @version 1.0
 */
@Service
@Transactional
public class KitmsUserService {

    private final Logger log = LoggerFactory.getLogger(KitmsUserService.class);

    @Autowired
    private KitmsUserRepository kitmsUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 모든 사용자 목록 조회
     */
    @Transactional(readOnly = true)
    public CustomReturnDTO getAllUsers() {
        try {
            List<KitmsUser> users = kitmsUserRepository.findAll();
            List<KitmsUserDTO> userDTOs = users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.OK);
            result.addColumn("list", userDTOs);
            return result;
        } catch (Exception e) {
            log.error("사용자 목록 조회 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("사용자 목록을 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 사용자 ID로 사용자 조회
     */
    @Transactional(readOnly = true)
    public CustomReturnDTO getUserById(String userId) {
        try {
            Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
            if (userOpt.isPresent()) {
                KitmsUserDTO userDTO = convertToDTO(userOpt.get());
                CustomReturnDTO result = new CustomReturnDTO();
                result.setStatus(HttpStatus.OK);
                result.addColumn("resultInfo", userDTO);
                return result;
            } else {
                CustomReturnDTO result = new CustomReturnDTO();
                result.setStatus(HttpStatus.NOT_FOUND);
                result.setMessage("사용자를 찾을 수 없습니다.");
                return result;
            }
        } catch (Exception e) {
            log.error("사용자 조회 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("사용자 조회 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 새 사용자 생성
     */
    public CustomReturnDTO createUser(KitmsUserDTO userDTO) {
        try {
            log.info("createUser 호출됨: userId={}, userName={}, userEmail={}", 
                    userDTO.getUserId(), userDTO.getUserName(), userDTO.getUserEmail());
            
            // 중복 ID 체크
            if (kitmsUserRepository.existsByUserIdIgnoreCase(userDTO.getUserId())) {
                CustomReturnDTO result = new CustomReturnDTO();
                result.setStatus(HttpStatus.BAD_REQUEST);
                result.setMessage("이미 존재하는 사용자 ID입니다.");
                return result;
            }

            // 이메일 중복 체크 (이메일이 있는 경우)
            if (userDTO.getUserEmail() != null && !userDTO.getUserEmail().isEmpty()) {
                if (kitmsUserRepository.existsByUserEmail(userDTO.getUserEmail())) {
                    CustomReturnDTO result = new CustomReturnDTO();
                    result.setStatus(HttpStatus.BAD_REQUEST);
                    result.setMessage("이미 존재하는 이메일입니다.");
                    return result;
                }
            }

            KitmsUser user = convertToEntity(userDTO);
            user.setCreateDt(ZonedDateTime.now());
            user.setCreateUserId("admin"); // 관리자가 생성

            KitmsUser savedUser = kitmsUserRepository.save(user);
            KitmsUserDTO savedUserDTO = convertToDTO(savedUser);

            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.OK);
            result.addColumn("resultInfo", savedUserDTO);
            result.setMessage("사용자가 성공적으로 생성되었습니다.");
            return result;
        } catch (Exception e) {
            log.error("사용자 생성 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("사용자 생성 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 사용자 정보 수정
     */
    public CustomReturnDTO updateUser(KitmsUserDTO userDTO) {
        try {
            Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userDTO.getUserId());
            if (!userOpt.isPresent()) {
                CustomReturnDTO result = new CustomReturnDTO();
                result.setStatus(HttpStatus.NOT_FOUND);
                result.setMessage("사용자를 찾을 수 없습니다.");
                return result;
            }

            KitmsUser existingUser = userOpt.get();
            
            // 이메일 중복 체크 (다른 사용자가 같은 이메일을 사용하는지)
            if (userDTO.getUserEmail() != null && !userDTO.getUserEmail().isEmpty()) {
                if (kitmsUserRepository.existsByUserEmail(userDTO.getUserEmail())) {
                    Optional<KitmsUser> emailUserOpt = kitmsUserRepository.findAll().stream()
                            .filter(u -> userDTO.getUserEmail().equals(u.getUserEmail()))
                            .findFirst();
                    if (emailUserOpt.isPresent() && !emailUserOpt.get().getUserId().equals(userDTO.getUserId())) {
                        CustomReturnDTO result = new CustomReturnDTO();
                        result.setStatus(HttpStatus.BAD_REQUEST);
                        result.setMessage("이미 다른 사용자가 사용 중인 이메일입니다.");
                        return result;
                    }
                }
            }

            // 기존 사용자 정보 업데이트
            existingUser.setUserName(userDTO.getUserName());
            existingUser.setUserEmail(userDTO.getUserEmail());
            existingUser.setUserTel(userDTO.getUserTel());
            existingUser.setUserMobile(userDTO.getUserMobile());
            existingUser.setAuthCode(userDTO.getAuthCode());
            existingUser.setUserRole(userDTO.getUserRole() != null ? userDTO.getUserRole() : userDTO.getAuthCode());
            existingUser.setEnable(userDTO.getEnable());
            existingUser.setUserEtc(userDTO.getUserEtc());
            
            // 필수 필드가 null인 경우 기본값 설정
            if (existingUser.getFirstFlag() == null) {
                existingUser.setFirstFlag(true);
            }
            if (existingUser.getOrganNo() == null) {
                existingUser.setOrganNo(1L);
            }

            // 비밀번호가 제공된 경우에만 업데이트
            if (userDTO.getUserPwd() != null && !userDTO.getUserPwd().isEmpty()) {
                existingUser.setUserPwd(passwordEncoder.encode(userDTO.getUserPwd()));
            }

            KitmsUser savedUser = kitmsUserRepository.save(existingUser);
            KitmsUserDTO savedUserDTO = convertToDTO(savedUser);

            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.OK);
            result.addColumn("resultInfo", savedUserDTO);
            result.setMessage("사용자 정보가 성공적으로 수정되었습니다.");
            return result;
        } catch (Exception e) {
            log.error("사용자 수정 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("사용자 수정 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 현재 로그인한 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public CustomReturnDTO getCurrentUser() {
        try {
            // Spring Security에서 현재 인증된 사용자 정보 가져오기
            org.springframework.security.core.Authentication authentication = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated()) {
                String userId = authentication.getName();
                Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
                
                if (userOpt.isPresent()) {
                    KitmsUserDTO userDTO = convertToDTO(userOpt.get());
                    CustomReturnDTO result = new CustomReturnDTO();
                    result.setStatus(HttpStatus.OK);
                    result.addColumn("resultInfo", userDTO);
                    return result;
                } else {
                    CustomReturnDTO result = new CustomReturnDTO();
                    result.setStatus(HttpStatus.NOT_FOUND);
                    result.setMessage("사용자 정보를 찾을 수 없습니다.");
                    return result;
                }
            } else {
                CustomReturnDTO result = new CustomReturnDTO();
                result.setStatus(HttpStatus.UNAUTHORIZED);
                result.setMessage("인증되지 않은 사용자입니다.");
                return result;
            }
        } catch (Exception e) {
            log.error("현재 사용자 정보 조회 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("현재 사용자 정보 조회 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * 사용자 삭제
     */
    public CustomReturnDTO deleteUser(String userId) {
        try {
            Optional<KitmsUser> userOpt = kitmsUserRepository.findKitmsUserByUserId(userId);
            if (!userOpt.isPresent()) {
                CustomReturnDTO result = new CustomReturnDTO();
                result.setStatus(HttpStatus.NOT_FOUND);
                result.setMessage("사용자를 찾을 수 없습니다.");
                return result;
            }

            kitmsUserRepository.delete(userOpt.get());

            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.OK);
            result.setMessage("사용자가 성공적으로 삭제되었습니다.");
            return result;
        } catch (Exception e) {
            log.error("사용자 삭제 중 오류 발생", e);
            CustomReturnDTO result = new CustomReturnDTO();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            result.setMessage("사용자 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }

    /**
     * KitmsUser 엔티티를 KitmsUserDTO로 변환
     */
    private KitmsUserDTO convertToDTO(KitmsUser user) {
        KitmsUserDTO dto = new KitmsUserDTO();
        dto.setUserNo(user.getUserNo());
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserTel(user.getUserTel());
        dto.setUserMobile(user.getUserMobile());
        dto.setAuthCode(user.getAuthCode());
        dto.setEnable(user.getEnable());
        dto.setUserEtc(user.getUserEtc());
        dto.setCreateDt(user.getCreateDt());
        dto.setCreateUserId(user.getCreateUserId());
        
        // 권한 코드를 역할로 변환
        dto.setUserRole(user.getUserRole() != null ? user.getUserRole() : user.getAuthCode());
        
        // 활성화 상태를 useFlag로 변환
        dto.setUseFlag(user.getEnable() ? "1" : "0");
        
        return dto;
    }

    /**
     * KitmsUserDTO를 KitmsUser 엔티티로 변환
     */
    private KitmsUser convertToEntity(KitmsUserDTO dto) {
        log.info("convertToEntity 호출됨: dto.userId={}, dto.userName={}, dto.userEmail={}", 
                dto.getUserId(), dto.getUserName(), dto.getUserEmail());
        
        KitmsUser user = new KitmsUser();
        user.setUserId(dto.getUserId());
        
        // userName이 null이거나 빈 문자열인 경우 userId를 사용
        String userName = dto.getUserName();
        if (userName == null || userName.trim().isEmpty()) {
            userName = dto.getUserId();
            log.warn("convertToEntity에서 userName이 null이므로 userId를 userName으로 사용: {}", userName);
        }
        user.setUserName(userName);
        
        user.setUserEmail(dto.getUserEmail());
        user.setUserTel(dto.getUserTel());
        user.setUserMobile(dto.getUserMobile());
        user.setAuthCode(dto.getAuthCode() != null ? dto.getAuthCode() : "0");
        user.setUserRole(dto.getUserRole() != null ? dto.getUserRole() : dto.getAuthCode());
        user.setEnable(dto.getEnable() != null ? dto.getEnable() : true);
        user.setUserEtc(dto.getUserEtc());
        
        // 필수 필드 설정
        user.setFirstFlag(true); // 첫 로그인 플래그 (기본값: true)
        user.setOrganNo(1L); // 기본 조직 번호 (기본값: 1)
        
        // 비밀번호 암호화
        if (dto.getUserPwd() != null && !dto.getUserPwd().isEmpty()) {
            user.setUserPwd(passwordEncoder.encode(dto.getUserPwd()));
        } else {
            // 기본 비밀번호 설정
            user.setUserPwd(passwordEncoder.encode("123456"));
        }
        
        return user;
    }
}

