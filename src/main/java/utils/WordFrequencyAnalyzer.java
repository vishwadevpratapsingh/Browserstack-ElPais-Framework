package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class WordFrequencyAnalyzer {

    public static final String TARGET_FOLDER = "./src/main/resources/TranslatedTitles";

    public static synchronized void saveTitlesToFile(List<String> titles) {
        try {
            // Ensure folder exists
            File dir = new File(TARGET_FOLDER);
            if (!dir.exists()) dir.mkdirs();

            // Create unique file
            String fileName = "translated_titles_" + System.currentTimeMillis() + ".txt";
            Path filePath = Paths.get(TARGET_FOLDER, fileName);

            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                for (String title : titles) {
                    writer.write(title);
                    writer.newLine();
                }
            }

            System.out.println("Titles written to: " + filePath.getFileName());
        } catch (IOException e) {
            System.out.println("Failed to write titles: " + e.getMessage());
        }
    }

    public static void analyzeFrequency(String unusedPathArg) {
        Map<String, Integer> wordMap = new HashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                Paths.get(TARGET_FOLDER), "translated_titles_*.txt")) {

            for (Path path : stream) {
                List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                for (String title : lines) {
                    if (title == null || title.isBlank()) continue;

                    String[] words = title.toLowerCase()
                                          .replaceAll("[^a-z\\s]", "")
                                          .split("\\s+");
                    for (String word : words) {
                        if (word.length() > 2) {
                            wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            }

            // Print repeated words (>2 times)
            System.out.println("\nFinal Repeated Words (more than 2 times):");
            boolean found = false;
            for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
                if (entry.getValue() > 2) {
                    System.out.println("" + entry.getKey() + ": " + entry.getValue());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No repeated words found.");
            }

        } catch (IOException e) {
            System.out.println("Error analyzing frequency: " + e.getMessage());
        }
    }
}
