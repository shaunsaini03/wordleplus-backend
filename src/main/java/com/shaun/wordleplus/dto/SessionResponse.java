package com.shaun.wordleplus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SessionResponse {

    private Long sessionId;
    private boolean finished;
    private List<GuessResponse> guesses;
    private int wordLength;
    private int maxGuesses;

}