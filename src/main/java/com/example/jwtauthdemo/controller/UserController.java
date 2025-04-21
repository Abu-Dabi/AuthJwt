package com.example.jwtauthdemo.controller;

import com.example.jwtauthdemo.util.JwtUtil;
import com.example.jwtauthdemo.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class UserController {

    private final JwtUtil jwtUtil;
    private final Map<String, String> users = new ConcurrentHashMap<>();

    // ✅ имя конструктора должно совпадать с именем класса
    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        users.put("user", "password");
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        if (users.containsKey(user.getUsername())) {
            throw new RuntimeException("Пользователь уже существует");
        }
        users.put(user.getUsername(), user.getPassword());
        return Map.of("message", "Регистрация успешна");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Логин и пароль обязательны");
        }

        if (password.equals(users.get(username))) {
            String token = jwtUtil.generateToken(username);
            return Map.of("token", token);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль");
        }
    }

    @GetMapping("/hello")
    public String hello() {
        return "Привет, ты авторизован!";
    }
}
