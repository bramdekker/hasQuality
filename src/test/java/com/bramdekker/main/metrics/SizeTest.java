package com.bramdekker.main.metrics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bramdekker.main.resources.FileList;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


// test-project
// 62 + 76 = 138 lines in total
// 12 + 15 = 27 blank lines
// 138 - 27 = 111 = LOC
// 31 + 27 = 58 comment lines
// 53 code lines
// Size in bytes: 2.683 + 2.146 = 4829 bytes
// Size in characters: 4829 characters
// Executable statements: 49
// Delivered Source instructions: 53
class SizeTest {
  static String pathToTestResources;
  static String sizeSection;

  @BeforeAll
  public static void setup() throws FileNotFoundException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/haskell-project");
    sizeSection = Size.getSection();
  }

  @Test
  void locMeasureIsCorrect() {
    assertTrue(sizeSection.contains("LOC: 111"));
  }

  @Test
  void nclocMeasureIsCorrect() {
    assertTrue(sizeSection.contains("NCLOC: 53"));
  }

  @Test
  void clocMeasureIsCorrect() {
    assertTrue(sizeSection.contains("CLOC: 58"));
  }

  @Test
  void blankLinesMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Blank lines: 27"));
  }

  @Test
  void sizeInBytesMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Size in bytes: 4829"));
  }

  @Test
  void sizeInCharactersMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Size in characters: 4829"));
  }

  @Test
  void avgModuleSizeMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Average module size (NCLOC): 27"));
  }

  @Test
  void maxModuleSizeMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Maximum module size (NCLOC): 30"));
  }
}
