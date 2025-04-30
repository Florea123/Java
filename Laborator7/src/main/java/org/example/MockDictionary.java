package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class MockDictionary {
    private final Set<String> words;

    public MockDictionary(String filePath) {
        words = new HashSet<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.toLowerCase().trim());
            }
            System.out.println("Dictionary loaded with " + words.size() + " words.");
        } catch (IOException e) {
            System.err.println("Error loading dictionary file: " + e.getMessage());
        }
    }

    public boolean isWord(String str) {
        return words.contains(str.toLowerCase().trim());
    }
}