package com.shaun.wordleplus.controller;

import com.shaun.wordleplus.dto.SessionResponse;
import com.shaun.wordleplus.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/today")
    public SessionResponse getTodaySession(@RequestParam String username) {
        return gameService.getTodaySession(username);
    }
}