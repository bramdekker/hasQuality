package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

// test-project
// Halstead length: 291 + 205 = 496
// Halstead vocabulary: (22 + 27) + (32 + 38) = 49 + 70 = 119
// Halstead volume: 3419.83
// avgHalsteadLength: 248.00
// maxHalsteadLength: 291
// maxHalsteadLengthName: Individual.hs
// avgHalsteadVocabulary: 59 (59.50)
// maxHalsteadVocabulary: 69 (70)
// maxHalsteadVocabularyName: Individual.hs
// avgHalsteadVolume: 1151.02 + 1783.62 / 2 = 1467.32
// maxHalsteadVolume: 1783.62
// maxHalsteadVolumeName: Individual.hs
class HalsteadTest {
  static String pathToTestResources;
  static String halsteadSection;

  @BeforeAll
  public static void setup() throws IOException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/haskell-project");
    halsteadSection = Halstead.getSection();
    System.out.println(halsteadSection);
  }

  @Test
  void halsteadLengthMeasureIsCorrect() {
    assertTrue(halsteadSection.contains("Halstead length: 496"));
  }

  @Test
  void avgHalsteadLengthIsCorrect() {
    assertTrue(halsteadSection.contains("Average Halstead length: 248.00"));
  }

  @Test
  void maxHalsteadLengthIsCorrect() {
    assertTrue(halsteadSection.contains("Maximum Halstead length: 291"));
  }

  @Test
  void maHalsteadLengthNameIsCorrect() {
    assertTrue(halsteadSection.matches("(?s).*Maximum Halstead length file:.*/Individual.hs.*$"));
  }

  @Test
  void halsteadVocabularyMeasureIsCorrect() {
    assertTrue(halsteadSection.contains("Halstead vocabulary: 119"));
  }

  @Test
  void avgHalsteadVocabularyIsCorrect() {
    assertTrue(halsteadSection.contains("Average Halstead vocabulary: 59.50"));
  }

  @Test
  void maxHalsteadVocabularyIsCorrect() {
    assertTrue(halsteadSection.contains("Maximum Halstead vocabulary: 70"));
  }

  @Test
  void maxHalsteadVocabularyNameIsCorrect() {
    assertTrue(halsteadSection.matches(
            "(?s).*Maximum Halstead vocabulary file:.*/Individual.hs.*$")
    );
  }

  @Test
  void halsteadVolumeMeasureIsCorrect() {
    assertTrue(halsteadSection.contains("Halstead volume: 3419.83"));
  }

  @Test
  void avgHalsteadVolumeIsCorrect() {
    assertTrue(halsteadSection.contains("Average Halstead volume: 1467.32"));
  }

  @Test
  void maxHalsteadVolumeIsCorrect() {
    assertTrue(halsteadSection.contains("Maximum Halstead volume: 1783.62"));
  }

  @Test
  void maxHalsteadVolumeNameIsCorrect() {
    assertTrue(halsteadSection.matches("(?s).*Maximum Halstead volume file:.*/Individual.hs.*$"));
  }
}
