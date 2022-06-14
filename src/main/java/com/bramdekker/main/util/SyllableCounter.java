package com.bramdekker.main.util;

import java.util.List;

/**
 * Class that exposes a static method to determine the number of syllables in an English word. It
 * is based on this theory (so not completely precise!):
 *    Starting y omitted:
 *      (a) when words begin with y, we don’t count the starting y as a vowel. (we are
 *      assuming there are no words that start with y followed immediately by a consonant)
 *    Silent e omitted:
 *      (a) when e is the last letter of a word, we’ll assume it is silent, unless the word is café
 *      or entrée (words borrowed from French). (* we’ll ignore all other words to simplify)
 *      For simplification, it may be best to create a new String without this silent e, before
 *      checking for more syllables.
 *    With the silent-e omitted, one-syllable units:
 *      (a) have a single vowel.
 *      (b) have two vowels that are the same letter in succession.
 *      (c) have two vowels in immediate succession that are ei, ie, ea, ou, ey, ay, oy, uy, ai.
 *     (d) have three vowels in immediate succession that are eau, iou (* yes, there are
 *      exceptions to this that we are ignoring here).
 *    With the silent-e omitted, two-syllable units:
 *      (a) two vowels in immediate succession where the vowels are different letters not
 *      following the rule above. For instance, oe, io, oi.
 *      (b) three vowels in immediate succession not following the rule above where the last
 *      vowel is not a silent e. For instance (“eye”) as in “meyer.”
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
