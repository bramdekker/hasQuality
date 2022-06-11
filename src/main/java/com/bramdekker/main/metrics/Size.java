package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import com.bramdekker.main.resources.HaskellParseTree;
import com.bramdekker.main.util.HalsteadVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.bramdekker.main.util.MathUtil.logN;
import static com.bramdekker.main.util.MetricPrinter.getMetricString;

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
  private static final List<FileMetric> dataPerFile = new ArrayList<>();
  private static long loc;
  private static long blankLines;
  private static long ncloc;
  private static long cloc;
  private static long dsi;
  private static long es;
  private static long bytes;
  private static long chars;
  private static long graphSize;
  private static long parseTreeSize;
  private static long avgModuleSize;
  private static long maxModuleSize;
  private static String maxModuleName;

  /**
   * Generate the section for size metrics.
   *
   * @return a report section containing information about size metrics as String.
   */
  public static String getSection() throws IOException {
    collectFileData();
    calculateMetrics();
    StringBuilder sizeSection = new StringBuilder("Size metrics:\n");

    sizeSection.append(getMetricString("LOC", loc));
    sizeSection.append(getMetricString("NCLOC", ncloc));
    sizeSection.append(getMetricString("CLOC", cloc));
    sizeSection.append(getMetricString("Executable statements", es));
    sizeSection.append(getMetricString("Delivered source instructions", dsi));
    sizeSection.append(getMetricString("Blank lines", blankLines));
    sizeSection.append(getMetricString("Size in bytes", bytes));
    sizeSection.append(getMetricString("Size in characters", chars));
    sizeSection.append(getMetricString("Number of nodes in the parse tree", parseTreeSize));

    if (dataPerFile.size() > 1) {
      sizeSection.append(getMetricString("Average module size (NCLOC)", avgModuleSize));
      sizeSection.append(getMetricString("Maximum module size (NCLOC)", maxModuleSize));
      sizeSection.append(String.format("Biggest module: %s", maxModuleName));
    }

    return sizeSection.toString();
  }

  /** Initializing all static variables of the class by setting them to 0. */
  private static void initializeMetrics() {
    loc = 0;
    cloc = 0;
    ncloc = 0;
    blankLines = 0;
    dsi = 0;
    es = 0;
    graphSize = 0;
    parseTreeSize = 0;
    bytes = 0;
    chars = 0;
    avgModuleSize = 0;
    maxModuleSize = 0;
  }

  /**
   * Collect all data per file needed to calculate metrics.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void collectFileData() throws IOException {
    for (File file : FileList.getInstance().getHaskellFiles()) {
      boolean inModuleExports = false;
      boolean inTypeSynonymOrDataType = false;

      Scanner curFileScanner = new Scanner(file);
      String curLine;
      int charsInFile = 0;
      int clocInFile = 0;
      int nclocInFile = 0;
      int blanklinesInFile = 0;
      int esInFile = 0;
      int dsiInFile = 0;
      while (curFileScanner.hasNextLine()) {
        curLine = curFileScanner.nextLine();

        // Line separator is excluded from nextLine, hence the +1.
        charsInFile += curLine.length() + 1;

        if (curLine.startsWith("module ")) {
          inModuleExports = true;
        } else if (curLine.startsWith("data ") || curLine.startsWith("type ")) {
          inTypeSynonymOrDataType = true;
        }

        if (!isBlank(curLine)) {
          if (inModuleExports || isImportOrPragma(curLine)) { // Heading
            nclocInFile++;
            dsiInFile++;
          } else if (inTypeSynonymOrDataType) { // Data declaration
            nclocInFile++;
            dsiInFile++;
          } else if (isComment(curLine)) { // Comment
            clocInFile++;
          } else { // Code line
            nclocInFile++;
            esInFile++;
            dsiInFile++;
          }
        } else {
          blanklinesInFile++;
        }

        if (inModuleExports && isEndOfModuleExport(curLine)) {
          inModuleExports = false;
        }

        if (inTypeSynonymOrDataType && isEndOfTypeSynonymOrDataType(curLine)) {
          inTypeSynonymOrDataType = false;
        }
      }

      dataPerFile.add(
          new FileMetric(
              file.getCanonicalPath(),
              file.length(),
              charsInFile,
              clocInFile,
              nclocInFile,
              blanklinesInFile,
              esInFile,
              dsiInFile));

      curFileScanner.close();
    }
  }

  /**
   * Calculate all size metrics and store them as static variables.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void calculateMetrics() throws IOException {
    initializeMetrics();

    sumFileData();

    parseTreeSize = calculateParseTreeSize();
    loc = ncloc + cloc;
    avgModuleSize = (long) Math.ceil((double) ncloc / dataPerFile.size());
    Optional<FileMetric> maxSize = dataPerFile.stream().max(Comparator.comparingLong(a -> a.ncloc));
    if (maxSize.isPresent()) {
      maxModuleSize = maxSize.get().ncloc;
      maxModuleName = maxSize.get().name;
    }
  }

  /**
   * Calculate the number of nodes in the parse tree.
   *
   * @return long representing the number of nodes in the parse tree.
   * @throws FileNotFoundException when a file in the FileList resource cannot be found.
   */
  private static long calculateParseTreeSize() throws IOException {
    long totalParseTreeSize = 0;
    for (ParseTree tree : HaskellParseTree.getInstance().getTreeDict().values()) {
      totalParseTreeSize += getChildren(tree) + 1;
    }

    return totalParseTreeSize;
  }

  /**
   * Get the number of children of a ParseTree node.
   *
   * @param node ParseTree where the number of children is calculated for.
   * @return long representing the number of children of node.
   */
  private static long getChildren(ParseTree node) {
    long childCount = 0;

    for (int i = 0; i < node.getChildCount(); i++) {
      childCount += getChildren(node.getChild(i)) + 1;
    }

    return childCount;
  }

  /** Sum the data per file to get overall metrics. */
  private static void sumFileData() {
    for (FileMetric metric : dataPerFile) {
      bytes += metric.bytes;
      chars += metric.chars;
      cloc += metric.cloc;
      ncloc += metric.ncloc;
      blankLines += metric.blankLines;
      es += metric.es;
      dsi += metric.dsi;
    }
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
  private static boolean isImportOrPragma(String line) {
    return line.startsWith("import ") || line.startsWith("{-#");
  }

  private static boolean isEndOfModuleExport(String line) {
    return line.equals("where") || line.endsWith(" where") || line.contains(" where ");
  }

  private static boolean isEndOfTypeSynonymOrDataType(String line) {
    return !Character.isWhitespace(line.charAt(0));
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
    return cloc;
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
    return dsi;
  }

  /**
   * Getter for the ES static variable.
   *
   * @return int representing the number of Executable Statements measure.
   */
  public static long getES() {
    return es;
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
    return loc;
  }

  /**
   * Getter for the NCLOC static variable.
   *
   * @return int representing the number of non-comment lines in the project.
   */
  public static long getNCLOC() {
    return ncloc;
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

  /**
   * Getter for the maxModuleName static variable.
   *
   * @return String representing the name of the biggest module in the project.
   */
  public static String getMaxModuleName() {
    return maxModuleName;
  }
}
