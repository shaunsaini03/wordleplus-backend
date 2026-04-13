package com.shaun.wordleplus.service;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Component
public class WordValidationService {

    private Set<String> dictionary;

    @PostConstruct
    public void loadDictionary() throws Exception {
        dictionary = new HashSet<>();
        ClassPathResource resource = new ClassPathResource("words.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (!line.isEmpty()) {
                    dictionary.add(line);
                }
            }
        }
    }

    public boolean isValidWord(String word) {
        return dictionary.contains(word.toLowerCase());
    }
}