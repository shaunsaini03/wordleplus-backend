package com.shaun.wordleplus.repository;

import com.shaun.wordleplus.model.Guess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuessRepository extends JpaRepository<Guess, Long> {

    List<Guess> findBySessionIdOrderByGuessNumber(Long sessionId);

}