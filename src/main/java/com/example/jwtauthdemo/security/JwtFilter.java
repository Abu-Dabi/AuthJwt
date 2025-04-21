package com.example.jwtauthdemo.security;

import com.example.jwtauthdemo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Извлекаем токен
            logger.info("Received Authorization header with Bearer token: {}", token);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                logger.info("Token is valid. Authenticating user: {}", username);

                // Создаем объект аутентификации для пользователя
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                logger.warn("Invalid token received: {}", token);
            }
        } else {
            logger.warn("Authorization header is missing or not Bearer token");
        }

        filterChain.doFilter(request, response); // Продолжаем фильтрацию
    }
}
