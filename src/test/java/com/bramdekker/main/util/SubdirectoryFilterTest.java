package com.bramdekker.main.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SubdirectoryFilterTest {
  static String pathToTestResources;

  @BeforeAll
  public static void setup() {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
  }

  @Test
  void acceptTrueOnDirectory() {
    File dir = new File(pathToTestResources + "/python-project");
    assertTrue(new SubdirectoryFilter().accept(dir, ""));
  }

  @Test
  void acceptFalseOnFile() {
    File dir = new File(pathToTestResources + "/haskell-project/individual.hs");
    assertFalse(new SubdirectoryFilter().accept(dir, "individual.hs"));
  }
}
