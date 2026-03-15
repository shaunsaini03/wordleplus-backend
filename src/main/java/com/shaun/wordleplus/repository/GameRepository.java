package com.shaun.wordleplus.repository;

import com.shaun.wordleplus.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findByGameDate(LocalDate gameDate);

}