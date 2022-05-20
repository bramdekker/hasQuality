package com.bramdekker.main.metrics;

import java.io.FileNotFoundException;

/**
 * Enum for all different metric types. The types all have a different section which can be accessed
 * via the getSection method.
 */
public enum MetricType {
  SIZE {
    public String getSection() throws FileNotFoundException {
      return Size.getSection();
    }
  },
  RECURSION {
    public String getSection() {
      return Recursion.getSection();
    }
  };

  public abstract String getSection() throws FileNotFoundException;
}
