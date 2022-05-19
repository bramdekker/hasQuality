package com.bramdekker.main.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HaskellFileFilterTest {
  static String pathToTestResources;

  @BeforeAll
  public static void setup() {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
  }

  @Test
  void acceptTrueOnHaskellFile() {
    File haskellFile = new File(pathToTestResources + "/haskell-project");
    assertTrue(new HaskellFileFilter().accept(haskellFile, "individual.hs"));
  }

  @Test
  void acceptTrueOnHaskellFile2() {
    File haskellFile = new File(pathToTestResources + "/haskell-project");
    assertTrue(new HaskellFileFilter().accept(haskellFile, "individual.lhs"));
  }

  @Test
  void acceptFalseOnNonHaskellFile() {
    File haskellFile = new File(pathToTestResources + "/haskell-project");
    assertFalse(new HaskellFileFilter().accept(haskellFile, "individual.txt"));
  }
}
