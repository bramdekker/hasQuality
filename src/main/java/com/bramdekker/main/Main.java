package com.bramdekker.main;

import com.bramdekker.main.configuration.Configuration;
import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.exceptions.InvalidPathnameException;
import com.bramdekker.main.report.Report;
import com.bramdekker.main.validator.Validator;

// test-project
// 62 + 76 = 138 lines in total
// 12 + 15 = 27 blank lines
// 111 = LOC
// (50 + 61) / 2 = 55.5 avg module size
// 61 = max module size
// 31 + 27 = 58 comment lines
// 53 code lines
// Size in bytes: 2.683 + 2.146 = 4829 bytes
// Size in characters: 4829 characters
// Executable statements: 49
// Delivered Source instructions: 53

// TODO: maybe different output options (csv file, terminal output, txt file)
// TODO: test single haskell file size metrics
// TODO: no flag for single haskell file. Collect data for individual files and then sum them up. Easier to expand
// for new metrics and more data is collected this way. MAx and avg are easily calculated for every metric

/**
 * All code in this repository follows the Google Java style (<a
 * href="https://google.github.io/styleguide/javaguide.html">https://google.github.io/styleguide/javaguide.html</a>).
 */
public class Main {
  /**
   * Main entrypoint of the application. Here the sequence of steps is executed needed to generate
   * a metrics report and exceptions are caught.
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
