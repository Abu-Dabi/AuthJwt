package com.example.jwtauthdemo.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final String secret = "MPlVrKgLmn4DHwbSAzZgblid6adaYZB0wx0dxiZ7YEjD63mfRa1f8cM3xMnora59WqX2iYvUHtVQ7pCwv8tvgw==";
    private final long expiration = 1000 * 60 * 60;

    public String generateToken(String username) {
        // Логирование генерации токена
        logger.info("Generating token for username: {}", username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String extractUsername(String token) {
        // Логирование извлечения имени пользователя из токена
        logger.info("Extracting username from token");
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            logger.info("Validating token: {}", token);
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true; // Если токен валиден
        } catch (JwtException e) {
            logger.error("Invalid token: {}", token, e); // Логируем ошибку с подробностями
            return false; // Токен невалиден
        }
    }
}
