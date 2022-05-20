package com.bramdekker.main.metrics;

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

// TODO: think of way to 'store' calculated metrics for further reuse. LOC = NCLOC + CLOC,
// comment density = CLOC / LOC
// TODO: suppress AbbreviationAsWordInName in checkstyle
/** Collection of methods that determine size metrics. */
public class Size {
  private static int LOC;
  private static int blankLines;
  private static int NCLOC;
  private static int CLOC;
  private static int DSI;
  private static int ES;
  private static int bytes;
  private static int chars;
  private static int graphSize;
  private static int parseTreeSize;


  /**
   * Generate the section for size metrics.
   *
   * @return a report section containing information about size metrics as String.
   */
  public static String getSection() throws FileNotFoundException {
    calculateMetrics();
    StringBuilder sizeSection = new StringBuilder("Size metrics: \n");

    sizeSection.append(String.format("LOC: %d\n", LOC));
    sizeSection.append(String.format("NCLOC: %d\n", NCLOC));
    sizeSection.append(String.format("CLOC: %d\n", CLOC));
    sizeSection.append(String.format("Blank lines: %d\n", blankLines));
    sizeSection.append(String.format("Size in bytes: %d\n", bytes));
    sizeSection.append(String.format("Size in characters: %d\n", chars));

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
  }

  // TODO: use parse tree to determine data declaration and header to calculate DSI / ES.

  /**
   * Calculate all size metrics and store them as static variables.
   *
   * @throws FileNotFoundException when a file in the FileList resource cannot be found.
   */
  private static void calculateMetrics() throws FileNotFoundException {
    initializeMetrics();

    for (File file : FileList.getInstance().getHaskellFiles()) {
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
            NCLOC++;
            ES++;
            DSI++;
          }
        } else {
          blankLines++;
        }
      }

      curFileScanner.close();
    }

    LOC = NCLOC + CLOC;
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
  public static int getChars() {
    return chars;
  }

  /**
   * Getter for the bytes static variable.
   *
   * @return int representing the size in bytes measure.
   */
  public static int getBytes() {
    return bytes;
  }

  /**
   * Getter for the CLOC static variable.
   *
   * @return int representing the number of comment lines measure.
   */
  public static int getCLOC() {
    return CLOC;
  }

  /**
   * Getter for the blankLines static variable.
   *
   * @return int representing the number of blank lines measure.
   */
  public static int getBlankLines() {
    return blankLines;
  }

  /**
   * Getter for the DSI static variable.
   *
   * @return int representing the number of Delivered Source Instructions measure.
   */
  public static int getDSI() {
    return DSI;
  }

  /**
   * Getter for the ES static variable.
   *
   * @return int representing the number of Executable Statements measure.
   */
  public static int getES() {
    return ES;
  }

  /**
   * Getter for the graphSize static variable.
   *
   * @return int representing the graph size measure.
   */
  public static int getGraphSize() {
    return graphSize;
  }

  /**
   * Getter for the LOC static variable.
   *
   * @return int representing the number of lines of code in the project.
   */
  public static int getLOC() {
    return LOC;
  }

  /**
   * Getter for the NCLOC static variable.
   *
   * @return int representing the number of non-comment lines in the project.
   */
  public static int getNCLOC() {
    return NCLOC;
  }

  /**
   * Getter for the parsetreeSize static variable.
   *
   * @return int representing the size of the parse tree.
   */
  public static int getParseTreeSize() {
    return parseTreeSize;
  }
}
