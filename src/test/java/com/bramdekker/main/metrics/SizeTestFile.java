package com.bramdekker.main.metrics;

//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.bramdekker.main.resources.FileList;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;


// individual.hs
// 76 lines in total
// 15 blank lines
// 61 = LOC
// 31 comment lines
// 30 code lines
// Size in bytes: 2.683 bytes
// Size in characters: 2683 characters
// Executable statements: 28
// Delivered Source instructions: 30
//class SizeTestFile {
//  static String pathToTestResources;
//  static String sizeSection;
//
//  @BeforeAll
//  public static void setup() throws IOException {
//    Path resourceDirectory = Paths.get("src", "test", "resources");
//    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
//    FileList.init(pathToTestResources + "/haskell-project/individual.hs");
//    sizeSection = Size.getSection();
//  }
//
//  @Test
//  void locMeasureIsCorrect() {
//    assertTrue(sizeSection.contains("LOC: 61"));
//  }
//
//  @Test
//  void nclocMeasureIsCorrect() {
//    assertTrue(sizeSection.contains("NCLOC: 30"));
//  }
//
//  @Test
//  void clocMeasureIsCorrect() {
//    assertTrue(sizeSection.contains("CLOC: 31"));
//  }
//
//  @Test
//  void blankLinesMeasureIsCorrect() {
//    assertTrue(sizeSection.contains("Blank lines: 15"));
//  }
//
//  @Test
//  void sizeInBytesMeasureIsCorrect() {
//    assertTrue(sizeSection.contains("Size in bytes: 2683"));
//  }
//
//  @Test
//  void sizeInCharactersMeasureIsCorrect() {
//    assertTrue(sizeSection.contains("Size in characters: 2683"));
//  }
//}
