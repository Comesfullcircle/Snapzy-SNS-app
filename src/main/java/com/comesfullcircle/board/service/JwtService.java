package com.comesfullcircle.board.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

import io.jsonwebtoken.Claims;


@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    // 비밀 키 생성
    private static final SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String getUsername(String accessToken){
        return getSubject(accessToken);
    }

    // 토큰 생성
    public String generateToken(String subject) {
        var now = new Date();
        var exp = new Date(now.getTime() + 1000 * 60 * 60 * 3); // 3시간 유효

        return Jwts.builder()
                .setSubject(subject) // 사용자 정보 설정
                .setIssuedAt(now) // 발행 시간
                .setExpiration(exp) // 만료 시간
                .signWith(key) // 서명 설정
                .compact();
    }

    // 토큰에서 Subject 추출
    public String getSubject(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(token) // 토큰 파싱
                    .getBody();

            return claims.getSubject(); // Subject 반환
        } catch (JwtException e) {
            logger.error("Invalid JWT token", e);
            throw new IllegalArgumentException("Invalid token provided", e);
        }
    }
}

