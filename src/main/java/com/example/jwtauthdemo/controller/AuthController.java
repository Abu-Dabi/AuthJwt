package com.example.jwtauthdemo.controller;

import com.example.jwtauthdemo.dto.AuthRequest;
import com.example.jwtauthdemo.entity.User;
import com.example.jwtauthdemo.service.JwtService;
import com.example.jwtauthdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null || !userService.passwordMatches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Неверный логин или пароль"));
        }

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request,
                                                       HttpServletResponse response) {
        String refreshToken = request.get("refreshToken");

        if (!jwtService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Невалидный refresh токен"));
        }

        String username = jwtService.extractUsername(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(username);

        // Создаем cookie для refreshToken
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true); // Безопасность: доступен только серверу
        refreshCookie.setSecure(true);   // Только через HTTPS
        refreshCookie.setPath("/");      // Доступен для всех путей
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // Время жизни куки - 7 дней

        // Добавляем cookie в ответ
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
