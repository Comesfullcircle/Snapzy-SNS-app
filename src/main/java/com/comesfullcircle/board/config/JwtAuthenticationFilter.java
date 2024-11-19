package com.comesfullcircle.board.config;

import com.comesfullcircle.board.exception.jwt.JwtTokentNotFoundException;
import com.comesfullcircle.board.service.JwtService;
import com.comesfullcircle.board.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // TODO : JWT 검증

        String BEARER_PREFIX = "Bearer";
        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        var securityContext = SecurityContextHolder.getContext();

       /* if(ObjectUtils.isEmpty(authorization) || !authorization.startsWith(BEARER_PREFIX)) {
            throw new JwtTokentNotFoundException();
        }*/

        if (!ObjectUtils.isEmpty(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            var accessToken = authorization.substring(BEARER_PREFIX.length());
            try {
                // 토큰에서 사용자 이름 추출
                var username = jwtService.getSubject(accessToken);

                // SecurityContext에 인증 정보가 없는 경우
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 사용자 정보 로드
                    var userDetails = userService.loadUserByUsername(username);

                    // 인증 토큰 생성
                    var authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // SecurityContext에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                logger.error("JWT Authentication failed", e); // 스택 트레이스 포함
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}