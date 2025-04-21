package com.example.jwtauthdemo.controller;

import com.example.jwtauthdemo.service.UserService;
import com.example.jwtauthdemo.util.JwtUtil;
import com.example.jwtauthdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Получить пользователя по имени
    @GetMapping("/users/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    // Получить всех пользователей
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Регистрация нового пользователя
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        try {
            userService.registerUser(user.getUsername(), user.getPassword());
            return Map.of("message", "Регистрация успешна");
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь уже существует");
        }
    }

    // Логин и получение токена
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (username == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Логин и пароль обязательны");
        }

        User foundUser = userService.findByUsername(username);
        if (userService.passwordMatches(password, foundUser.getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);
            return Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            );
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный логин или пароль");
        }
    }


    @GetMapping("/hello")
    public String hello() {
        return "Привет, ты авторизован!";
    }
}
