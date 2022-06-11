package com.bramdekker.main.util;

import java.util.List;

/**
 * Class that exposes a static method to determine the number of syllables in an English word.
 */
public class SyllableCounter {
  private static final List<String> oneSyllableTwoVowels =
      List.of("aa", "oo", "ee", "ei", "ie", "ea", "ou", "ey", "ay", "oy", "uy", "ai");
  private static final List<String> oneSyllableThreeVowels = List.of("eau", "iou");

  /**
   * Determines the number of syllables in the given (English!) word.
   *
   * @param word the word to be analyzed.
   * @return the number of syllables in the word.
   */
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

    cleanedWord = cleanedWord.toLowerCase();

    int syllables = 0;
    String currentVowels = "";
    for (int i = 0; i < cleanedWord.length(); i++) {
      if (!Character.isLetter(cleanedWord.charAt(i))) {
        continue;
      }

      if (isVowel(cleanedWord.charAt(i))) {
        currentVowels += cleanedWord.charAt(i);
      } else {
        syllables += countSyllablesInVowelString(currentVowels);
        currentVowels = "";
      }
    }

    if (!currentVowels.isEmpty()) {
      if (currentVowels.equals("ue")) {
        syllables++;
      } else {
        syllables += countSyllablesInVowelString(currentVowels);
      }
    }

    return syllables;
  }

  private static int countSyllablesInVowelString(String vowels) {
    if (vowels.length() == 2 && oneSyllableTwoVowels.contains(vowels)) {
      return 1;
    } else if (vowels.length() == 3 && oneSyllableThreeVowels.contains(vowels)) {
      return 1;
    } else if (vowels.length() == 3) {
      return 2;
    }

    return vowels.length();
  }

  private static boolean isVowel(char c) {
    return "aeiouy".contains(String.valueOf(c));
  }
}
