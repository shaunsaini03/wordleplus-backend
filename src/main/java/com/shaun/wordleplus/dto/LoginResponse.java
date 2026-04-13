package com.shaun.wordleplus.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private Long userId;
    private String username;
    private int numWins;
    private int highScore;
    private String token;

}