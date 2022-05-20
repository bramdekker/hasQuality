package com.bramdekker.main.resources;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class FileListTest {
  static String pathToTestResources;

  @BeforeAll
  public static void setup() {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
  }

  @Test
  void getAllHaskellFilesInHaskellProjectDirectory() {
    FileList.init(pathToTestResources + "/haskell-project");

    List<File> expectedFiles =
        List.of(
            new File(pathToTestResources + "/haskell-project/individual.hs"),
            new File(pathToTestResources + "/haskell-project/Puzzles.hs"));
    List<File> otherFiles =
        List.of(
            new File(pathToTestResources + "/haskell-project/haskell_college_handouts.pdf"),
            new File(pathToTestResources + "/haskell-project/PT_2018_Haskell_Slides.pdf"));
    List<File> haskellFiles = FileList.getInstance().getHaskellFiles();

    expectedFiles.forEach(file -> assertTrue(haskellFiles.contains(file)));
    otherFiles.forEach(file -> assertFalse(haskellFiles.contains(file)));
  }
}
