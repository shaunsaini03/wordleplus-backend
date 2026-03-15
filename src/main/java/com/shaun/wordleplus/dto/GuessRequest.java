package com.shaun.wordleplus.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuessRequest {

    private Long sessionId;
    private String guessWord;

}