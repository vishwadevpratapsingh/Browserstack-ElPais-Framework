package utils;

import java.util.*;

public class TextAnalysisUtil {

    public static void analyzeRepeatedWords(List<String> titles) {
        Map<String, Integer> wordCount = new HashMap<>();

        for (String title : titles) {
            String[] words = title.toLowerCase()
                                  .replaceAll("[^a-z ]", "")  // Remove punctuation
                                  .split("\\s+");             // Split by space

            for (String word : words) {
                if (!word.isEmpty()) {
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

        System.out.println("Repeated Words:");
        boolean found = false;
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > 2) {
                System.out.println("" + entry.getKey() + ": " + entry.getValue() + " times");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No word was repeated.");
        }
    }
}
