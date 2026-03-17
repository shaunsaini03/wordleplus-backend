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
    SELECT u.username, COUNT(s)
    FROM User u
    LEFT JOIN GameSession s ON s.user = u AND s.won = true
    GROUP BY u.username
    ORDER BY COUNT(s) DESC
""")
    List<Object[]> globalLeaderboard();

    @Query("""
SELECT u.username,
       COUNT(g),
       CASE 
           WHEN COUNT(CASE WHEN g.guessWord = s.game.word THEN 1 END) > 0 
           THEN true 
           ELSE false 
       END
FROM User u
LEFT JOIN GameSession s 
    ON s.user = u AND s.game.gameDate = :date
LEFT JOIN Guess g 
    ON g.session = s
GROUP BY u.username
ORDER BY 
    CASE 
        WHEN COUNT(CASE WHEN g.guessWord = s.game.word THEN 1 END) > 0 
        THEN 0 ELSE 1 
    END,
    COUNT(g)
""")
    List<Object[]> dailyLeaderboard(LocalDate date);

}