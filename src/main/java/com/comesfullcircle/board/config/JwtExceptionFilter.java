package com.comesfullcircle.board.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (JwtException exception){
            //  jwt 관련 커스텀 에러 메세지 생성
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");

            // JSON으로 응답할 오류 메시지
            var errorMap = new HashMap<String, Object>();
            errorMap.put("status", HttpStatus.UNAUTHORIZED);
            errorMap.put("message", exception.getMessage());

            // ObjectMapper를 사용해 Map을 JSON 형식으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(errorMap);
            response.getWriter().write(responseJson);

        }

    }
}