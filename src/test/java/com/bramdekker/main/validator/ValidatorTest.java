package com.bramdekker.main.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.exceptions.InvalidPathnameException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
  void validateReturnsFalseWhenHelpFlagIsUsed()
      throws InvalidPathnameException, InvalidCommandException {
    assertFalse(new Validator().validate(new String[] {"-h"}));
  }

  @Test
  void validateReturnsFalseWhenHelpFlagIsUsed2()
      throws InvalidPathnameException, InvalidCommandException {
    assertFalse(new Validator().validate(new String[] {"--help"}));
  }

  @Test
  void validateFailsWhenNoArgumentGiven() {
    Assertions.assertThrows(
        InvalidCommandException.class, () -> new Validator().validate(new String[] {}));
  }

  @Test
  void validateFailsWhenExistingFileGiven() {
    String dir = pathToTestResources + "/haskell-project/individual.hs";

    Assertions.assertThrows(
        InvalidPathnameException.class, () -> new Validator().validate(new String[] {dir}));
  }

  @Test
  void validateFailsWhenNonExistingDirectoryGiven() {
    Assertions.assertThrows(
        InvalidPathnameException.class,
        () -> new Validator().validate(new String[] {"qwlieurjhsadg"}));
  }

  @Test
  void validateFailsWhenExistingDirectoryDoesNotContainHaskellFiles() {
    String dir = pathToTestResources + "/python-project";

    Assertions.assertThrows(
        InvalidPathnameException.class, () -> new Validator().validate(new String[] {dir}));
  }
}
