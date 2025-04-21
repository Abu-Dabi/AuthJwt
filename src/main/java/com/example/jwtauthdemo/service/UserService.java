package com.example.jwtauthdemo.service;

import com.example.jwtauthdemo.entity.User;
import com.example.jwtauthdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Регистрация нового пользователя
    public void registerUser(String username, String password) {
        // Проверка, существует ли уже пользователь с таким именем
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        // Кодирование пароля
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword);

        // Сохранение пользователя в базе данных
        userRepository.save(user);
    }

    // Поиск пользователя по имени
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь с таким именем не найден"));
    }

    // Получение всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Проверка, совпадает ли пароль с закодированным
    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
