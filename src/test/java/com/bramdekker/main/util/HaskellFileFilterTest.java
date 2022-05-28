package com.bramdekker.main.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


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
  void acceptFalseOnLiteralHaskellFile() {
    File haskellFile = new File(pathToTestResources + "/haskell-project");
    assertFalse(new HaskellFileFilter().accept(haskellFile, "individual.lhs"));
  }

  @Test
  void acceptFalseOnNonHaskellFile() {
    File haskellFile = new File(pathToTestResources + "/haskell-project");
    assertFalse(new HaskellFileFilter().accept(haskellFile, "individual.txt"));
  }
}
