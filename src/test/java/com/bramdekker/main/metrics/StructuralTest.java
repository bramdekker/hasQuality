package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StructuralTest {
  static String pathToTestResources;
  static String structuralSection;

  @BeforeAll
  public static void setup() throws IOException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/haskell-project");
    structuralSection = Structural.getSection();
    System.out.println(structuralSection);
  }

  @Test
  void modulesFunctionsMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Modules / functions: 0.10"));
  }

  @Test
  void modulesDataTypesMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Modules / data types: Infinity"));
  }

  @Test
  void modulesTypeSynonymsMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Modules / type synonyms: 2.00"));
  }

  @Test
  void avgFunctionsInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Average number of functions per module: 10.50"));
  }

  @Test
  void maxFunctionsInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Maximum number of functions in module: 12"));
  }

  @Test
  void maxFunctionsInModuleNameIsCorrect() {
    assertTrue(
        structuralSection.matches(
            "(?s).*Maximum number of functions in module name: .*/Individual.hs.*$"));
  }

  @Test
  void minFunctionsInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Minimum number of functions in module: 9"));
  }

  @Test
  void minFunctionsInModuleNameIsCorrect() {
    assertTrue(
        structuralSection.matches(
            "(?s).*Minimum number of functions in module name: .*/Puzzles.hs.*$"));
  }

  @Test
  void avgDataTypesInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Average number of data types per module: 0.00"));
  }

  @Test
  void maxDataTypesInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Maximum number of data types in module: 0"));
  }

  @Test
  void maxDataTypesInModuleNameIsCorrect() {
    assertTrue(
            structuralSection.matches(
                    "(?s).*Maximum number of data types in module name: .*$"));
  }

  @Test
  void minDataTypesInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Minimum number of data types in module: 0"));
  }

  @Test
  void minDataTypesInModuleNameIsCorrect() {
    assertTrue(
            structuralSection.matches(
                    "(?s).*Minimum number of data types in module name: .*/Puzzles.hs.*$"));
  }

  @Test
  void avgTypeSynonymsInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Average number of type synonyms per module: 0.50"));
  }

  @Test
  void maxTypeSynonymsInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Maximum number of type synonyms in module: 1"));
  }

  @Test
  void maxTypeSynonymsInModuleNameIsCorrect() {
    assertTrue(
            structuralSection.matches(
                    "(?s).*Maximum number of type synonyms in module name: .*/Individual.hs.*$"));
  }

  @Test
  void minTypeSynonymsInModuleMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Minimum number of type synonyms in module: 0"));
  }

  @Test
  void minTypeSynonymsInModuleNameIsCorrect() {
    assertTrue(
            structuralSection.matches(
                    "(?s).*Minimum number of data types in module name: .*/Puzzles.hs.*$"));
  }
}
