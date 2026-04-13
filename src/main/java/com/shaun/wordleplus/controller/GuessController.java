package com.shaun.wordleplus.controller;

import com.shaun.wordleplus.service.GameService;
import com.shaun.wordleplus.dto.GuessResponse;
import com.shaun.wordleplus.dto.GuessRequest;

import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/guess")
public class GuessController {

    private final GameService gameService;

    public GuessController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public GuessResponse submitGuess(@RequestBody GuessRequest request) {
        return gameService.submitGuess(request);
    }
}
