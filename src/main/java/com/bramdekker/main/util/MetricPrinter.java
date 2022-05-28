package com.bramdekker.main.util;

/** Class for printing information about metrics. */
public class MetricPrinter {
  /**
   * Print a single line with a description and a value about a certain metric.
   *
   * @param description String with the description of the metric.
   * @param value long with the value of the metric.
   * @return String with a single line of text giving information about the metric.
   */
  public static String getMetricString(String description, long value) {
    return String.format("%s: %d\n", description, value);
  }
}
