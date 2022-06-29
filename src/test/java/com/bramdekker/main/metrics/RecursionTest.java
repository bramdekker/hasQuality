package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RecursionTest {
  static String pathToTestResources;
  static String recursionSection;

  @BeforeAll
  public static void setup() throws IOException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/haskell-project");
    recursionSection = Recursion.getSection();
    System.out.println(recursionSection);
  }

  @Test
  void numRecursiveFunctionsMeasureIsCorrect() {
    assertTrue(recursionSection.contains("Number of recursive functions: 3"));
  }

  @Test
  void numTrivialRecursiveFunctionsMeasureIsCorrect() {
    assertTrue(recursionSection.contains("Number of trivial recursive functions: 3"));
  }

  @Test
  void numNonTrivialRecursiveFunctionsMeasureIsCorrect() {
    assertTrue(recursionSection.contains("Number of non-trivial recursive functions: 0"));
  }

  @Test
  void longestNonTrivialPathMeasureIsCorrect() {
    assertTrue(recursionSection.contains("Longest non-trivial call chain: 0"));
  }

  @Test
  void longestNonTrivialPathNameMeasureIsCorrect() {
    assertTrue(recursionSection.contains("Longest non-trivial call chain function names: "));
  }

  @Test
  void recursiveFunctionRatioMeasureIsCorrect() {
    assertTrue(recursionSection.contains("Ratio recursive/non-recursive functions: 0.14"));
  }

  @Test
  void maxRecursiveFunctionsMeasureIsCorrect() {
    assertTrue(recursionSection.contains("Maximum number of recursive functions: 2"));
  }

  @Test
  void maxRecursiveFunctionsNameMeasureIsCorrect() {
    assertTrue(
        recursionSection.matches(
            "(?s).*Maximum number of recursive functions module name: .*Puzzles.*$"));
  }
}
