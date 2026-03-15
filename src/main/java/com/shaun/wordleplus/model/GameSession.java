package com.shaun.wordleplus.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_sessions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"}))
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private int numGuesses;

    private boolean won;

    private boolean score;

    private boolean finished;
}