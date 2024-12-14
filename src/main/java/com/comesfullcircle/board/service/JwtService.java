package com.comesfullcircle.board.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    //private static final SecretKey key = Jwts.SIG.HS256.key().build();
    // 32바이트 이상의 시크릿 키 생성
    private static final SecretKey key = Keys.hmacShaKeyFor("your-secure-secret-key-your-secure-secret-key".getBytes(StandardCharsets.UTF_8));

    public String getUsername(String jwtToken) {
        return getSubject(jwtToken);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    private String generateToken(String subject) {
        var now = new Date();
        var exp = new Date(now.getTime() + (1000 * 60 * 60 * 3));
        //return Jwts.builder().subject(subject).signWith(key).issuedAt(now).expiration(exp).compact();
        return Jwts.builder()
                .setSubject(subject) // setSubject 사용
                .signWith(key) // 서명 키 설정
                .setIssuedAt(now) // 발행일
                .setExpiration(exp) // 만료일
                .compact();
    }

    private String getSubject(String token) {
        try {
          /*  return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();*/

            return Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 키 설정
                    .build()
                    .parseClaimsJws(token) // JWT 토큰 파싱
                    .getBody() // 클레임의 Body 가져오기
                    .getSubject(); // Subject 추출

        } catch (JwtException e) {
            logger.error("JwtException", e);
            throw e;
        }
    }
}