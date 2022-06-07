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
  RECURSION {
    public String getSection() {
      return Recursion.getSection();
    }
  },
  PATTERNS {
    public String getSection() {
      return Patterns.getSection();
    }
  },
  USABILITY {
    public String getSection() {
      return Usability.getSection();
    }
  };

  public abstract String getSection() throws IOException;
}
