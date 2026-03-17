package com.shaun.wordleplus.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyLeaderboardEntry {
    private String username;
    private Integer numGuesses; // nullable
    private boolean won;
    private Integer maxGuesses;
}
