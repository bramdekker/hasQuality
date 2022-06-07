package com.bramdekker.main.util;

public class SyllableCounter {
    public static int numberOfSyllables(String word) {
        String cleanedWord;
        if (word.startsWith("y") && word.endsWith("e")) {
            cleanedWord = word.substring(1, word.length() - 1);
        } else if (word.startsWith("y")) {
            cleanedWord = word.substring(1);
        } else if (word.endsWith("e")) {
            cleanedWord = word.substring(0, word.length() - 1);
        } else {
            cleanedWord = word;
        }

        int syllables = 0;
        String substring;
        for (int i = 0; i < cleanedWord.length(); i++) {
            if ()
        }
    }

    private static boolean isVowel(char c) {
        return "aeiouy".contains(String.valueOf(c));
    }
}
