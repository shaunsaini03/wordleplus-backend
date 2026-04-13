package com.shaun.wordleplus.controller;

import com.shaun.wordleplus.dto.LoginRequest;
import com.shaun.wordleplus.dto.LoginResponse;
import com.shaun.wordleplus.dto.RegisterRequest;
import com.shaun.wordleplus.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}