package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternsTest {
  static String pathToTestResources;
  static String patternsSection;

  @BeforeAll
  public static void setup() throws IOException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/haskell-project");
    patternsSection = Patterns.getSection();
    System.out.println(patternsSection);
  }

  @Test
  void avgNumberOfVariablesMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Average number of variables: 1.43"));
  }

  @Test
  void avgNumberOfConstructorsMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Average number of constructors: 0"));
  }

  @Test
  void avgNumberOfWildcardsMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Average number of wildcards: 0"));
  }

  @Test
  void wildcardVariablesRatioMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Wildcard-variables ratio: 0.00"));
  }

  @Test
  void avgDepthOfNestingMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Average depth of nesting: 0.13"));
  }

  @Test
  void maxDepthOfNestingMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Maximum depth of nesting: 1"));
  }

  @Test
  void maxDepthOfNestingNameIsCorrect() {
    assertTrue(
        patternsSection.matches(
            "(?s).*Maximum depth of nesting function name: Puzzles.isPalindrome.*$"));
  }

  @Test
  void avgSumDepthOfNestingMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Average sum of depth of nesting: 0.13"));
  }

  @Test
  void maxSumDepthOfNestingMeasureIsCorrect() {
    assertTrue(patternsSection.contains("Maximum sum of depth of nesting: 1"));
  }

  @Test
  void maxSumDepthOfNestingNameIsCorrect() {
    assertTrue(
        patternsSection.matches(
            "(?s).*Maximum sum of depth of nesting function name: Puzzles.isPalindrome.*$"));
  }
}
