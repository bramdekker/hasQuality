package com.bramdekker.main.metrics;

import java.io.IOException;

/**
 * Enum for all different metric types. The types all have a different section which can be accessed
 * via the getSection method.
 */
public enum MetricType {
  SIZE {
    public String getSection() throws IOException {
      return Size.getSection();
    }
  },
  HALSTEAD {
    public String getSection() throws IOException {
      return Halstead.getSection();
    }
  },
  RECURSION {
    public String getSection() {
      return Recursion.getSection();
    }
  };

  public abstract String getSection() throws IOException;
}
