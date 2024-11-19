package com.comesfullcircle.board.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }
        catch (JarException exception) {
            //TODO : JWT 관련 커스텀 에러메시지 생성해 RESPONSE 로 내려주기
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");

            var errorMap = new HashMap<String, Object>();
            errorMap.put("status", HttpStatus.UNAUTHORIZED);
            errorMap.put("message", exception.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String responseJson = objectMapper.writeValueAsString(errorMap);
            response.getWriter().print(responseJson);
        }

        filterChain.doFilter(request, response);
    }
}