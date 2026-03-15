package com.shaun.wordleplus.repository;

import com.shaun.wordleplus.model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {

    Optional<GameSession> findByUserIdAndGameId(Long userId, Long gameId);


    @Query("""
        SELECT s.user.username, COUNT(s)
        FROM GameSession s
        WHERE s.won = true
        GROUP BY s.user.username
        ORDER BY COUNT(s) DESC
        """)
    List<Object[]> globalLeaderboard();

    @Query("""
        SELECT s.user.username, MIN(g.guessNumber)
        FROM GameSession s
        JOIN Guess g ON g.session = s
        WHERE s.won = true
        AND s.game.gameDate = :date
        AND g.guessWord = s.game.word
        GROUP BY s.user.username
        ORDER BY MIN(g.guessNumber) ASC
        """)
    List<Object[]> dailyLeaderboard(LocalDate date);

}