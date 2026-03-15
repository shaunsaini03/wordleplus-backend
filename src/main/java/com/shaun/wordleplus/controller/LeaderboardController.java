package com.shaun.wordleplus.controller;

import com.shaun.wordleplus.dto.GlobalLeaderboardEntry;
import com.shaun.wordleplus.dto.DailyLeaderboardEntry;
import com.shaun.wordleplus.service.GameService;
import java.util.List;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/leaderboard")
@CrossOrigin
public class LeaderboardController {

    private final GameService gameService;

    public LeaderboardController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/global")
    public List<GlobalLeaderboardEntry> global() {
        return gameService.getGlobalLeaderboard();
    }

    @GetMapping("/daily")
    public List<DailyLeaderboardEntry> daily() {
        return gameService.getDailyLeaderboard();
    }

}