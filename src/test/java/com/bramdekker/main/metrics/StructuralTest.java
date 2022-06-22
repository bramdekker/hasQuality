package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

// Branches in functions: (1 + 2 + 2 + 1 + 1 + 1 + 1 + 4 + 1) + (1 + 2 + 2 + 1 + 1 + 1 + 1 + 3 + 1 +
// 1 + 1 + 4) = 33
// Average cyclomatic complexity = 1.57 + 1
// Maximum cyclomatic complexity = 5
// Maximum cyclomatic complexity name: Individual.addList / Puzzles.isPalindrome
// 254 - 6 - 2 = 246 operators / 21 functions = 11.71
// 242 - 5 = 237 operands / 21 functions = 11.29
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
  void avgCyclomaticComplexityMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Average cyclomatic complexity: 2.62"));
  }

  @Test
  void maxCyclomaticComplexityMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Maximum cyclomatic complexity: 6"));
  }

  @Test
  void maxCyclomaticComplexityNameIsCorrect() {
    assertTrue(
        structuralSection.matches("(?s).*Maximum cyclomatic complexity name: Individual.addList.*$")
            || structuralSection.matches(
                "(?s).*Maximum cyclomatic complexity name: Puzzles.isPalindrome.*$"));
  }

  @Test
  void avgOperatorsMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Average number of operators per function: 11.71"));
  }

  @Test
  void maxOperatorsMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Maximum number of operators: 24"));
  }

  @Test
  void maxOperatorsNameIsCorrect() {
    assertTrue(
        structuralSection.matches(
            "(?s).*Maximum number of operators name: Individual.getIndex'.*$"));
  }

  @Test
  void avgOperandsMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Average number of operands per function: 11.29"));
  }

  @Test
  void maxOperandsMeasureIsCorrect() {
    assertTrue(structuralSection.contains("Maximum number of operands: 28"));
  }

  @Test
  void maxOperandsNameIsCorrect() {
    assertTrue(
        structuralSection.matches(
            "(?s).*Maximum number of operands name: Individual.getIndex'.*$"));
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
    assertTrue(structuralSection.matches("(?s).*Maximum number of data types in module name: .*$"));
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
