package com.bramdekker.main.validator;

import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.exceptions.InvalidPathnameException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {
  static String pathToTestResources;

  @BeforeAll
  public static void setup() {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
  }

  @Test
  void validateSucceedsWhenValidDirectoryGiven()
      throws InvalidPathnameException, InvalidCommandException {
    String dir = pathToTestResources + "/haskell-project";
    assertTrue(new Validator().validate(new String[] {dir}));
  }

  @Test
  void validateSucceedsWhenValidFlagsAndDirectoryGiven()
      throws InvalidPathnameException, InvalidCommandException {
    String dir = pathToTestResources + "/haskell-project";
    assertTrue(new Validator().validate(new String[] {"-s", "-r", dir}));
  }

  @Test
  void validateSucceedsWhenHaskellFileGiven()
      throws InvalidPathnameException, InvalidCommandException {
    String dir = pathToTestResources + "/haskell-project/Individual.hs";
    assertTrue(new Validator().validate(new String[] {dir}));
  }

  @Test
  void validateSucceedsWhenValidFlagsAndHaskellFileGiven()
      throws InvalidPathnameException, InvalidCommandException {
    String dir = pathToTestResources + "/haskell-project/Individual.hs";
    assertTrue(new Validator().validate(new String[] {"-s", "-r", dir}));
  }

  @Test
  void validateReturnsFalseWhenHelpFlagIsUsed()
      throws InvalidPathnameException, InvalidCommandException {
    assertFalse(new Validator().validate(new String[] {"--help"}));
  }

  @Test
  void validateFailsWhenNoHaskellFileIsGiven() {
    String dir = pathToTestResources + "/haskell-project/haskell_college_handouts.pdf";
    assertThrows(
        InvalidPathnameException.class, () -> new Validator().validate(new String[] {dir}));
  }

  @Test
  void validateFailsWhenNonExistingHaskellFileGiven()
      throws InvalidPathnameException, InvalidCommandException {
    String dir = pathToTestResources + "/haskell-project/hello.hs";
    assertThrows(
        InvalidPathnameException.class, () -> new Validator().validate(new String[] {dir}));
  }

  @Test
  void validateFailsWhenNoArgumentGiven() {
    assertThrows(InvalidCommandException.class, () -> new Validator().validate(new String[] {}));
  }

  @Test
  void validateFailsWhenNonExistingDirectoryGiven() {
    assertThrows(
        InvalidPathnameException.class,
        () -> new Validator().validate(new String[] {"qwlieurjhsadg"}));
  }

  @Test
  void validateFailsWhenExistingDirectoryDoesNotContainHaskellFiles() {
    String dir = pathToTestResources + "/python-project";

    assertThrows(
        InvalidPathnameException.class, () -> new Validator().validate(new String[] {dir}));
  }
}
