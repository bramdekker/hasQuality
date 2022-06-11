package com.bramdekker.main.metrics;

/** Collection of methods that determine recursion metrics. */
public class Recursion {
  /**
   * Generate the section for recursion metrics.
   *
   * @return a report section containing information about recursion metrics as String.
   */
  public static String getSection() {
    StringBuilder recursionSection = new StringBuilder("Recursion metrics:\n");
    return recursionSection.toString();
  }
}
