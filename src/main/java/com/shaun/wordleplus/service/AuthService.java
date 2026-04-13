package com.shaun.wordleplus.service;

import com.shaun.wordleplus.dto.LoginRequest;
import com.shaun.wordleplus.dto.LoginResponse;
import com.shaun.wordleplus.dto.RegisterRequest;
import com.shaun.wordleplus.model.User;
import com.shaun.wordleplus.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UsernameValidator usernameValidator;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtService jwtService, UsernameValidator usernameValidator) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.usernameValidator = usernameValidator;
    }

    public LoginResponse register(RegisterRequest request) {
        String username = request.getUsername().toLowerCase().trim();

        usernameValidator.validate(username);

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setNumWins(0);
        user.setHighScore(0);
        user = userRepository.save(user);

        return buildResponse(user);
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername().toLowerCase().trim();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        return buildResponse(user);
    }

    private LoginResponse buildResponse(User user) {
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNumWins(user.getNumWins());
        response.setHighScore(user.getHighScore());
        response.setToken(jwtService.generateToken(user.getUsername()));
        return response;
    }
}