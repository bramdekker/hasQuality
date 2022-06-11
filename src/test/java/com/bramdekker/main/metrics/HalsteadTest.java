package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

// test-project
// Halstead length: 292 + 205 = 497
// Halstead vocabulary:
// Halstead volume:
// avgHalsteadLength: 249
// maxHalsteadLength: 292
// maxHalsteadLengthName: individual.hs
// avgHalsteadVocabulary:
// maxHalsteadVocabulary:
// maxHalsteadVocabularyName:
// avgHalsteadVolume:
// maxHalsteadVolume:
// maxHalsteadVolumeName:
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
    assertTrue(halsteadSection.contains("Halstead length: 497"));
  }

  @Test
  void avgHalsteadLengthIsCorrect() {
    assertTrue(halsteadSection.contains("Average Halstead length: 249"));
  }

  @Test
  void maxHalsteadLengthIsCorrect() {
    assertTrue(halsteadSection.contains("Maximum Halstead length: 292"));
  }

  @Test
  void maxModuleSizeNameIsCorrect() {
    assertTrue(halsteadSection.matches("(?s).*Maximum Halstead length file:.*/individual.hs.*$"));
  }
}
