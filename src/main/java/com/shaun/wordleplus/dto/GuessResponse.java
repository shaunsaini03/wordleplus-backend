package com.shaun.wordleplus.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuessResponse {

    private String guessWord;
    private String result;
    private int guessNumber;

}