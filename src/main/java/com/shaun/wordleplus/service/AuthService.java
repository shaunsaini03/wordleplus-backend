package com.shaun.wordleplus.service;

import com.shaun.wordleplus.dto.LoginRequest;
import com.shaun.wordleplus.dto.LoginResponse;
import com.shaun.wordleplus.model.User;
import com.shaun.wordleplus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername().toLowerCase().trim();

        Optional<User> userOptional = userRepository.findByUsername(username);

        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User();
            user.setUsername(username);
            user.setNumWins(0);
            user.setHighScore(0);

            user = userRepository.save(user);
        }

        LoginResponse response = new LoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNumWins(user.getNumWins());
        response.setHighScore(user.getHighScore());

        return response;
    }
}