package com.shaun.wordleplus.controller;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
