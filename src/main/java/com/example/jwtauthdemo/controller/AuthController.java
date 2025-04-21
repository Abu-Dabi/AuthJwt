package com.example.jwtauthdemo.controller;

import com.example.jwtauthdemo.dto.AuthRequest;
import com.example.jwtauthdemo.entity.User;
import com.example.jwtauthdemo.service.JwtService;
import com.example.jwtauthdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Неверный логин или пароль"));
        }

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }


    @GetMapping("/hello")
    public String hello() {
        return "Привет, ты авторизован!";
    }
}
