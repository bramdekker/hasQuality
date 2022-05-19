package com.bramdekker.main.validator;

import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.exceptions.InvalidPathnameException;
import com.bramdekker.main.util.HaskellFileFilter;
import com.bramdekker.main.util.SubdirectoryFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Provides methods for validating the pathname entered by the user. The pathname should point to a
 * directory that exists and contains at least 1 Haskell file (.hs or .lhs extension).
 */
public class Validator {
  private static final FilenameFilter haskellFilter = new HaskellFileFilter();
  private static final FilenameFilter subdirectoriesFilter = new SubdirectoryFilter();

  /**
   * Check if a File object is an existing directory containing a Haskell project.
   *
   * @param args command line argument representing a path to a Haskell project directory.
   * @return true if a report should be generated; false otherwise.
   * @throws InvalidPathnameException if pathname is not a valid one.
   * @throws InvalidCommandException if program is not invoked correctly.
   */
  public boolean validate(String[] args) throws InvalidPathnameException, InvalidCommandException {
    if (hasNoArgument(args)) {
      throw new InvalidCommandException(
          "Expected at least 1 argument!\nUsage: gradle run [flags] <path-to-haskell-project-dir>");
    }

    // Check for help flag and just return if it is.
    if (args[0].equals("-h") || args[0].equals("--help")) {
      printHelp();
      return false;
    }

    // Get the pathname from all the arguments.
    String rawPathname = getPathname(args);
    File pathname = new File(rawPathname);
    if (!isDirectory(pathname)) {
      throw new InvalidPathnameException("Pathname must point to an existing directory!");
    }
    if (!dirContainsHaskell(pathname)) {
      throw new InvalidPathnameException("Directory must contain at least 1 Haskell file!");
    }

    return true;
  }


  /**
   * Print a help message for the user. It lists all flags and their functionality.
   */
  private void printHelp() {
    String helpMessage = """
                USAGE: ./gradle run [flags] <path-to-haskell-project-dir>
                
                Flags:
                    -s              Compute and report on size metrics
                    -r              Compute and report on recursion metrics
                    -h / --help     Show this help message
                """;
    System.out.println(helpMessage.trim());
  }

  /**
   * Retrieves the pathname from the other arguments. There should be exactly one non-flag argument
   * and this should be the last one.
   *
   * @param args List with all command line arguments.
   * @return the pathname that should be analysed.
   * @throws InvalidCommandException if arguments are missing/ordered wrongly.
   */
  private String getPathname(String[] args) throws InvalidCommandException {
    for (int i = 0; i < args.length; i++) {
      // Non-final arguments should be flags.
      if (i < args.length - 1 && !isFlag(args[i])) {
        throw new InvalidCommandException("Pathname should be the last argument!");
      }

      // Final argument should be the pathname and not a flag.
      if (i == args.length - 1 && isFlag(args[i])) {
        throw new InvalidCommandException("Pathname should be the last argument!");
      }
    }

    return args[args.length - 1];
  }

  /**
   * Checks if the command line argument is a flag or not.
   *
   * @param arg command line argument
   * @return true if arg is a flag; false otherwise.
   */
  private Boolean isFlag(String arg) {
    return arg.startsWith("-");
  }

  /**
   * Check if the number of command line arguments is 1.
   *
   * @param args array of Strings containing all command line arguments.
   * @return true if there is exactly 1 command line argument; false otherwise.
   */
  private Boolean hasNoArgument(String[] args) {
    return args.length < 1;
  }

  /**
   * Check if a File object is an existing directory.
   *
   * @param projectPath File object representing a Haskell project directory.
   * @return true if path is an existing directory; false otherwise.
   */
  private Boolean isDirectory(File projectPath) {
    return projectPath.isDirectory();
  }

  /**
   * Check if a File object contains at least 1 Haskell file.
   *
   * @param projectPath File object representing a Haskell project directory.
   * @return true if projectPath contains a Haskell file.
   */
  private Boolean dirContainsHaskell(File projectPath) {
    // Get all Haskell files in the directory.
    File[] files = projectPath.listFiles(haskellFilter);

    if (files != null && files.length > 0) {
      return true;
    } else { // no Haskell files in current directory
      File[] subDirectories = projectPath.listFiles(subdirectoriesFilter);
      if (subDirectories != null && subDirectories.length > 0) {
        return Arrays.stream(subDirectories).anyMatch(this::dirContainsHaskell);
      }
    }

    return false;
  }
}
