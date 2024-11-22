package com.comesfullcircle.board.config;

import com.comesfullcircle.board.exception.jwt.JwtTokenNotFoundException;
import com.comesfullcircle.board.service.JwtService;
import com.comesfullcircle.board.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 특정 경로는 JWT 인증을 적용하지 않음
        String path = request.getRequestURI();
        if ("/api/v1/users".equals(path) || "/api/v1/users/authenticate".equals(path)) {
            if (HttpMethod.POST.matches(request.getMethod())) {
                filterChain.doFilter(request, response);
                return; // 이 경로에 대해선 JWT 인증을 하지 않음
            }
        }

        String BEARER_PREFIX = "Bearer ";
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var securityContext = SecurityContextHolder.getContext();
        if(ObjectUtils.isEmpty(authorization)|| !authorization.startsWith(BEARER_PREFIX)){
            throw new JwtTokenNotFoundException();
        }
        if (!ObjectUtils.isEmpty(authorization) && authorization.startsWith(BEARER_PREFIX)
                && securityContext.getAuthentication() == null){
            var accessToken = authorization.substring(BEARER_PREFIX.length());
            var username = jwtService.getUsername(accessToken);
            var userDetails = userService.loadUserByUsername(username);

            var authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);
        }

        filterChain.doFilter(request,response);
    }
}