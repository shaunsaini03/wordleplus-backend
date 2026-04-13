package com.shaun.wordleplus.service;

import com.shaun.wordleplus.dto.GuessResponse;
import com.shaun.wordleplus.dto.GuessRequest;
import com.shaun.wordleplus.dto.SessionResponse;
import com.shaun.wordleplus.model.Game;
import com.shaun.wordleplus.model.GameSession;
import com.shaun.wordleplus.model.Guess;
import com.shaun.wordleplus.model.User;
import com.shaun.wordleplus.repository.GameRepository;
import com.shaun.wordleplus.repository.GameSessionRepository;
import com.shaun.wordleplus.repository.GuessRepository;
import com.shaun.wordleplus.repository.UserRepository;
import com.shaun.wordleplus.dto.GlobalLeaderboardEntry;
import com.shaun.wordleplus.dto.DailyLeaderboardEntry;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final GameSessionRepository sessionRepository;
    private final GuessRepository guessRepository;
    private final WordValidationService wordValidationService;

    public GameService(UserRepository userRepository,
                       GameRepository gameRepository,
                       GameSessionRepository sessionRepository,
                       GuessRepository guessRepository,
                       WordValidationService wordValidationService) {

        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.sessionRepository = sessionRepository;
        this.guessRepository = guessRepository;
        this.wordValidationService = wordValidationService;
    }

    public SessionResponse getTodaySession(String username) {

        username = username.toLowerCase().trim();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Game game = gameRepository.findByGameDate(LocalDate.now(ZoneId.of("America/Los_Angeles")))
                .orElseThrow(() -> new RuntimeException("Game not created"));

        GameSession session = sessionRepository
                .findByUserIdAndGameId(user.getId(), game.getId())
                .orElseGet(() -> {
                    GameSession newSession = new GameSession();
                    newSession.setUser(user);
                    newSession.setGame(game);
                    newSession.setFinished(false);
                    return sessionRepository.save(newSession);
                });

        List<Guess> guesses =
                guessRepository.findBySessionIdOrderByGuessNumber(session.getId());

        List<GuessResponse> guessResponses =
                guesses.stream().map(g -> {
                    GuessResponse dto = new GuessResponse();
                    dto.setGuessWord(g.getGuessWord());
                    dto.setResult(g.getResult());
                    dto.setGuessNumber(g.getGuessNumber());
                    return dto;
                }).collect(Collectors.toList());

        SessionResponse response = new SessionResponse();
        response.setSessionId(session.getId());
        response.setFinished(session.isFinished());
        response.setGuesses(guessResponses);
        response.setWordLength(game.getWord().length());
        response.setMaxGuesses(game.getMaxGuesses());

        return response;
    }

    private String evaluateGuess(String guess, String answer) {

        int length = answer.length();

        char[] result = new char[length];
        char[] answerChars = answer.toCharArray();
        char[] guessChars = guess.toCharArray();

        boolean[] used = new boolean[length];

        // pass 1: greens
        for (int i = 0; i < length; i++) {
            if (guessChars[i] == answerChars[i]) {
                result[i] = 'G';
                used[i] = true;
            }
        }

        // pass 2: yellows / blacks
        for (int i = 0; i < length; i++) {

            if (result[i] == 'G') continue;

            boolean found = false;

            for (int j = 0; j < length; j++) {
                if (!used[j] && guessChars[i] == answerChars[j]) {
                    found = true;
                    used[j] = true;
                    break;
                }
            }

            result[i] = found ? 'Y' : 'B';
        }

        return new String(result);
    }

    public GuessResponse submitGuess(GuessRequest request) {

        GameSession session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.isFinished()) {
            throw new RuntimeException("Game already finished");
        }

        Game game = session.getGame();
        String answer = game.getWord();

        String guessWord = request.getGuessWord().toLowerCase();

        if (guessWord.length() != answer.length()) {
            throw new RuntimeException("Guess must be " + answer.length() + " letters");
        }

        if (!wordValidationService.isValidWord(guessWord)) {
            throw new RuntimeException("Not a valid word");
        }

        int guessNumber =
                guessRepository.findBySessionIdOrderByGuessNumber(session.getId()).size() + 1;

        String result = evaluateGuess(guessWord, answer);

        Guess guess = new Guess();
        guess.setSession(session);
        guess.setGuessWord(guessWord);
        guess.setGuessNumber(guessNumber);
        guess.setResult(result);

        guessRepository.save(guess);

        boolean won = guessWord.equals(answer);

        if (won || guessNumber == game.getMaxGuesses()) {
            session.setWon(won);
            session.setFinished(true);
            sessionRepository.save(session);
        }

        GuessResponse response = new GuessResponse();
        response.setGuessWord(guessWord);
        response.setResult(result);
        response.setGuessNumber(guessNumber);
        return response;
    }

    public List<GlobalLeaderboardEntry> getGlobalLeaderboard() {

        List<Object[]> rows = sessionRepository.globalLeaderboard();

        return rows.stream().map(r -> {
            GlobalLeaderboardEntry e = new GlobalLeaderboardEntry();
            e.setUsername((String) r[0]);
            e.setWins(((Number) r[1]).intValue());
            return e;
        }).toList();
    }

    public List<DailyLeaderboardEntry> getDailyLeaderboard() {

        LocalDate today = LocalDate.now(ZoneId.of("America/Los_Angeles"));
        Game game = gameRepository.findByGameDate(LocalDate.now(ZoneId.of("America/Los_Angeles")))
                .orElseThrow(() -> new RuntimeException("Game not created"));

        List<Object[]> rows = sessionRepository.dailyLeaderboard(today);

        return rows.stream().map(r -> {
            DailyLeaderboardEntry e = new DailyLeaderboardEntry();
            e.setUsername((String) r[0]);

            e.setNumGuesses(r[1] != null ? ((Number) r[1]).intValue() : null);
            e.setWon((Boolean) r[2]);
            e.setMaxGuesses(game.getMaxGuesses());

            return e;
        }).toList();
    }
}