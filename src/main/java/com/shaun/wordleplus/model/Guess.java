package com.shaun.wordleplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "guesses")
public class Guess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guessWord;

    private int guessNumber;

    private String result;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private GameSession session;
}