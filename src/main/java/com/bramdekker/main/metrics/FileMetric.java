package com.bramdekker.main.metrics;

/** Encapsulates data for a specific file. */
public class FileMetric {
  public String name;
  public long bytes;
  public long chars;
  public long cloc;
  public long ncloc;
  public long blankLines;
  public long es;
  public long dsi;
  public long halsteadLength;
  public long halsteadVocabulary;

  /**
   * Set all metrics for the file.
   *
   * @param name name of the file as String.
   * @param bytes number of bytes of the file.
   * @param chars number of characters in the file.
   * @param cloc number of comment lines in the file.
   * @param ncloc number of non-comment lines in the file.
   * @param blankLines number of blanklines in the file.
   * @param es number of executable statements in the file.
   * @param dsi number of delivered source instruction in the file.
   * @param halsteadLength total occurrences of operators + total occurrences of operands.
   * @param halsteadVocabulary unique occurrences of operators + unique occurrences of operands.
   */
  public FileMetric(
      String name,
      long bytes,
      long chars,
      long cloc,
      long ncloc,
      long blankLines,
      long es,
      long dsi,
      long halsteadLength,
      long halsteadVocabulary) {
    this.name = name;
    this.bytes = bytes;
    this.chars = chars;
    this.cloc = cloc;
    this.ncloc = ncloc;
    this.blankLines = blankLines;
    this.es = es;
    this.dsi = dsi;
    this.halsteadLength = halsteadLength;
    this.halsteadVocabulary = halsteadVocabulary;
  }
}
