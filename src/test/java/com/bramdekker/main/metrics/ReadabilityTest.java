package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadabilityTest {
  static String pathToTestResources;
  static String readabilitySection;

  @BeforeAll
  public static void setup() throws IOException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/haskell-project");
    readabilitySection = Readability.getSection();
    System.out.println(readabilitySection);
  }

  @Test
  void commentDensityMeasureIsCorrect() {
    assertTrue(readabilitySection.contains("Comment density: 0.52"));
  }

  @Test
  void avgCommentDensityMeasureIsCorrect() {
    assertTrue(readabilitySection.contains("Average comment density: 0.52"));
  }

  @Test
  void maxCommentDensityMeasureIsCorrect() {
    assertTrue(readabilitySection.contains("Maximum comment density: 0.54"));
  }

  @Test
  void minCommentDensityMeasureIsCorrect() {
    assertTrue(readabilitySection.contains("Minimum comment density: 0.51"));
  }

  @Test
  void maxCommentDensityNameIsCorrect() {
    assertTrue(readabilitySection.matches("(?s).*Maximum comment density file: .*/Puzzles.hs.*$"));
  }

  @Test
  void minCommentDensityNameIsCorrect() {
    assertTrue(
        readabilitySection.matches("(?s).*Minimum comment density file: .*/Individual.hs.*$"));
  }

  @Test
  void fogIndexMeasureIsCorrect() {
    assertTrue(readabilitySection.contains("Gunning's fog index: 14"));
  }

  @Test
  void avgFogIndexMeasureIsCorrect() {
    assertTrue(readabilitySection.contains("Average Gunning's fog index: 14"));
  }

  @Test
  void maxFogIndexMeasureIsCorrect() {
    assertTrue(readabilitySection.contains("Maximum Gunning's fog index: 15"));
  }

  @Test
  void maxFogIndexNameIsCorrect() {
    assertTrue(
        readabilitySection.matches("(?s).*Maximum Gunning's fog index file: .*/Puzzles.hs.*$"));
  }
}
