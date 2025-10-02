package com.kone.kitms.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * KITMS JWT 토큰 생성 및 검증을 담당하는 클래스
 * 
 * 이 클래스는 KITMS 시스템의 JWT 토큰 관리를 담당합니다:
 * - JWT 토큰 생성 및 서명
 * - JWT 토큰 유효성 검증
 * - 토큰에서 사용자 정보 추출
 * - 토큰 만료 시간 관리
 * - 보안 키 기반 토큰 암호화/복호화
 * 
 * @author KITMS Development Team
 * @version 1.0
 * @since 2024
 */
// JWT 기능을 일시적으로 비활성화 (설정 누락으로 인한 오류 방지)
// @Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtSecret;

    @Value("${jhipster.security.authentication.jwt.token-validity-in-seconds}")
    private int jwtExpirationInMs;

    /**
     * JWT 토큰 생성
     * 
     * @param authentication Spring Security 인증 객체
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs * 1000L);

        // 사용자 권한을 문자열로 변환
        String authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .claim("username", userPrincipal.getUsername())
                .claim("email", userPrincipal.getEmail())
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 사용자 ID로 JWT 토큰 생성
     * 
     * @param userId 사용자 ID
     * @param username 사용자명
     * @param authorities 권한 목록
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(Long userId, String username, String authorities) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs * 1000L);

        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .claim("username", username)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * JWT 토큰 유효성 검증
     * 
     * @param authToken 검증할 JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (SecurityException ex) {
            log.error("JWT 서명이 유효하지 않습니다");
        } catch (MalformedJwtException ex) {
            log.error("JWT 토큰이 잘못되었습니다");
        } catch (ExpiredJwtException ex) {
            log.error("JWT 토큰이 만료되었습니다");
        } catch (UnsupportedJwtException ex) {
            log.error("지원하지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException ex) {
            log.error("JWT 클레임이 비어있습니다");
        }
        return false;
    }

    /**
     * JWT 토큰에서 사용자 ID 추출
     * 
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * JWT 토큰에서 사용자명 추출
     * 
     * @param token JWT 토큰
     * @return 사용자명
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("username", String.class);
    }

    /**
     * JWT 토큰에서 권한 정보 추출
     * 
     * @param token JWT 토큰
     * @return 권한 정보 문자열
     */
    public String getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("authorities", String.class);
    }

    /**
     * JWT 토큰에서 만료 시간 추출
     * 
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    /**
     * JWT 토큰이 만료되었는지 확인
     * 
     * @param token JWT 토큰
     * @return 만료되었으면 true, 그렇지 않으면 false
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * JWT 서명에 사용할 키 생성
     * 
     * @return SecretKey 객체
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰 만료 시간 반환 (초 단위)
     * 
     * @return 토큰 만료 시간 (초)
     */
    public int getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
}
