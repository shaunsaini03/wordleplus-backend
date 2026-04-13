package com.shaun.wordleplus.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class UsernameValidator {

    private static final Pattern ALLOWED = Pattern.compile("^[a-z0-9_]{3,20}$");

    private Set<String> bannedWords;

    @PostConstruct
    public void loadBannedWords() throws Exception {
        bannedWords = new HashSet<>();
        ClassPathResource resource = new ClassPathResource("banned_words.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (!line.isEmpty()) {
                    bannedWords.add(line);
                }
            }
        }
    }

    public void validate(String username) {
        if (!ALLOWED.matcher(username).matches()) {
            throw new RuntimeException("Username must be 3–20 characters and contain only letters, numbers, or underscores");
        }
        for (String word : bannedWords) {
            if (username.contains(word)) {
                throw new RuntimeException("Username is not allowed");
            }
        }
    }
}
