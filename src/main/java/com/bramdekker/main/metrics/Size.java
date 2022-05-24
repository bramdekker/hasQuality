package com.bramdekker.main.metrics;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;

import com.bramdekker.main.resources.FileList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// - lines of code (LOC)
//      -> noncommented lines (NCLOC)
//      - Blank lines -> not counted
//      - Comment lines -> counted separately (CLOC)
//      - Data declarations
//      - Lines that contain several separate instructions
// - number of executable statements (ES)
//      - comment lines, data declarations, and headings are ignored
//      - count separate statements on the same physical line as distinct
// - number of delivered source instructions (DSI)
//      - it counts separate statements on the same physical line as distinct
//      - ignores comment lines
// - number of characters (wc -m <filename>)
// - number in bytes
// - graph with modules/statements as nodes and control flow/data links as edges

/** Collection of methods that determine size metrics. */
public class Size {
  private static long LOC;
  private static long blankLines;
  private static long NCLOC;
  private static long CLOC;
  private static long DSI;
  private static long ES;
  private static long bytes;
  private static long chars;
  private static long graphSize;
  private static long parseTreeSize;
  private static long avgModuleSize;
  private static long maxModuleSize;


  /**
   * Generate the section for size metrics.
   *
   * @return a report section containing information about size metrics as String.
   */
  public static String getSection() throws FileNotFoundException {
    calculateMetrics();
    StringBuilder sizeSection = new StringBuilder("Size metrics: \n");

    sizeSection.append(getMetricString("LOC", LOC));
    sizeSection.append(getMetricString("NCLOC", NCLOC));
    sizeSection.append(getMetricString("CLOC", CLOC));
    sizeSection.append(getMetricString("Blank lines", blankLines));
    sizeSection.append(getMetricString("Size in bytes", bytes));
    sizeSection.append(getMetricString("Size in characters", chars));
    sizeSection.append(getMetricString("Average module size (NCLOC)", avgModuleSize));
    sizeSection.append(getMetricString("Maximum module size (NCLOC)", maxModuleSize));

    return sizeSection.toString();
  }

  /**
   * Initializing all static variables of the class by setting them to 0.
   */
  private static void initializeMetrics() {
    LOC = 0;
    CLOC = 0;
    NCLOC = 0;
    blankLines = 0;
    DSI = 0;
    ES = 0;
    graphSize = 0;
    parseTreeSize = 0;
    bytes = 0;
    chars = 0;
    avgModuleSize = 0;
    maxModuleSize = 0;
  }

  // TODO: use parse tree to determine data declaration and header to calculate DSI / ES.

  /**
   * Calculate all size metrics and store them as static variables.
   *
   * @throws FileNotFoundException when a file in the FileList resource cannot be found.
   */
  private static void calculateMetrics() throws FileNotFoundException {
    initializeMetrics();
    int numberOfModules = 0;
    int currentModuleSize = 0;

    for (File file : FileList.getInstance().getHaskellFiles()) {
      numberOfModules++;

      bytes += file.length();
      Scanner curFileScanner = new Scanner(file);
      String curLine;
      while (curFileScanner.hasNextLine()) {
        curLine = curFileScanner.nextLine();

        // Line separator is excluded from nextLine, hence the +1.
        chars += curLine.length() + 1;

        if (!isBlank(curLine)) {
          if (isComment(curLine)) {
            CLOC++;
          } else { // Code line
            currentModuleSize++;
            NCLOC++;
            ES++;
            DSI++;
          }
        } else {
          blankLines++;
        }
      }

      curFileScanner.close();

      // Determine if this is the new maximum module size.
      if (currentModuleSize > maxModuleSize) {
        maxModuleSize = currentModuleSize;
      }
      currentModuleSize = 0;
    }

    LOC = NCLOC + CLOC;
    avgModuleSize = (long) Math.ceil((double) NCLOC / numberOfModules);
  }

  /**
   * Checks whether a line is a Haskell comment.
   *
   * @param line String representing a line.
   * @return true if line is a comment; false otherwise.
   */
  private static boolean isComment(String line) {
    return line.startsWith("--");
  }

  /**
   * Checks whether a line is a blank line.
   *
   * @param line String representing a line.
   * @return true if line is a blank line; false otherwise.
   */
  private static boolean isBlank(String line) {
    return line.trim().isEmpty();
  }

  /**
   * Checks whether a line is a header line.
   *
   * @param line String representing a line.
   * @return true if line is a header line; false otherwise.
   */
  private static boolean isHeader(String line) {
    return line.startsWith("import") || line.startsWith("where") || line.startsWith("module");
  }

  /**
   * Checks whether a line is a data declaration.
   *
   * @param line String representing a line.
   * @return true if line is a data declaration; false otherwise.
   */
  private static boolean isDataDeclaration(String line) {
    return false;
  }

  /**
   * Getter for the chars static variable.
   *
   * @return int representing the size in characters measure.
   */
  public static long getChars() {
    return chars;
  }

  /**
   * Getter for the bytes static variable.
   *
   * @return int representing the size in bytes measure.
   */
  public static long getBytes() {
    return bytes;
  }

  /**
   * Getter for the CLOC static variable.
   *
   * @return int representing the number of comment lines measure.
   */
  public static long getCLOC() {
    return CLOC;
  }

  /**
   * Getter for the blankLines static variable.
   *
   * @return int representing the number of blank lines measure.
   */
  public static long getBlankLines() {
    return blankLines;
  }

  /**
   * Getter for the DSI static variable.
   *
   * @return int representing the number of Delivered Source Instructions measure.
   */
  public static long getDSI() {
    return DSI;
  }

  /**
   * Getter for the ES static variable.
   *
   * @return int representing the number of Executable Statements measure.
   */
  public static long getES() {
    return ES;
  }

  /**
   * Getter for the graphSize static variable.
   *
   * @return int representing the graph size measure.
   */
  public static long getGraphSize() {
    return graphSize;
  }

  /**
   * Getter for the LOC static variable.
   *
   * @return int representing the number of lines of code in the project.
   */
  public static long getLOC() {
    return LOC;
  }

  /**
   * Getter for the NCLOC static variable.
   *
   * @return int representing the number of non-comment lines in the project.
   */
  public static long getNCLOC() {
    return NCLOC;
  }

  /**
   * Getter for the parsetreeSize static variable.
   *
   * @return int representing the size of the parse tree.
   */
  public static long getParseTreeSize() {
    return parseTreeSize;
  }

  /**
   * Getter for the avgModuleSize static variable.
   *
   * @return int representing the average size of a module in the project.
   */
  public static long getAvgModuleSize() {
    return avgModuleSize;
  }

  /**
   * Getter for the maxModuleSize static variable.
   *
   * @return int representing the maximum size of a module in the project.
   */
  public static long getMaxModuleSize() {
    return maxModuleSize;
  }
}
