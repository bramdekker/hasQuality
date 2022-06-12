package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

// test-project
// 62 + 76 = 138 lines in total
// 12 + 15 = 27 blank lines
// 111 = LOC
// (30 + 23) / 2 = 26.5 -> 27 avg module size
// 30 = max module size
// 31 + 27 = 58 comment lines
// 53 code lines
// Size in bytes: 2.686 + 2.146= 4832 bytes
// Size in characters: 4832 characters
// Executable statements: 27 + 21 = 48
// Delivered Source instructions: 53
// Biggest module: Individual.hs
class SizeTest {
  static String pathToTestResources;
  static String sizeSection;

  @BeforeAll
  public static void setup() throws IOException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/haskell-project");
    sizeSection = Size.getSection();
    System.out.println(sizeSection);
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
  void esMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Executable statements: 48"));
  }

  @Test
  void dsiMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Delivered source instructions: 53"));
  }

  @Test
  void blankLinesMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Blank lines: 27"));
  }

  @Test
  void sizeInBytesMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Size in bytes: 4832"));
  }

  @Test
  void sizeInCharactersMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Size in characters: 4832"));
  }

  @Test
  void avgModuleSizeMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Average module size (NCLOC): 27"));
  }

  @Test
  void maxModuleSizeMeasureIsCorrect() {
    assertTrue(sizeSection.contains("Maximum module size (NCLOC): 30"));
  }

  @Test
  void maxModuleSizeNameIsCorrect() {
    assertTrue(sizeSection.matches("(?s).*Maximum module size file: .*/Individual.hs.*$"));
  }
}
