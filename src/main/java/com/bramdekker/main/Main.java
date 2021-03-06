package com.bramdekker.main;

import com.bramdekker.main.configuration.Configuration;
import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.exceptions.InvalidPathnameException;
import com.bramdekker.main.report.Report;
import com.bramdekker.main.validator.Validator;

/**
 * All code in this repository follows the Google Java style (<a
 * href="https://google.github.io/styleguide/javaguide.html">https://google.github.io/styleguide/javaguide.html</a>).
 */
public class Main {
  /**
   * Main entrypoint of the application. Here the sequence of steps is executed needed to generate a
   * metrics report and exceptions are caught.
   *
   * @param args a String array with command line arguments.
   */
  public static void main(String[] args) {
    // Maybe use flags for help and/or turning on/off certain metrics.
    try {
      // Check if program is invoked correctly and if given pathname points to a Haskell project.
      boolean canContinue = new Validator().validate(args);
      if (!canContinue) {
        return;
      }

      // Create the configuration object.
      Configuration config = new Configuration(args);

      // Create and display report based on configuration.
      Report report = new Report(config);
      report.display();
    } catch (InvalidPathnameException | InvalidCommandException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      System.err.println("Something went wrong: " + e.getMessage());
    }
  }
}
