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
    public String getSection() throws IOException {
      return Recursion.getSection();
    }
  },
  PATTERNS {
    public String getSection() throws IOException {
      return Patterns.getSection();
    }
  },
  READABILITY {
    public String getSection() throws IOException {
      return Readability.getSection();
    }
  },
  CALLGRAPH {
    public String getSection() throws IOException {
      return Callgraph.getSection();
    }
  },
  STRUCTURAL {
    public String getSection() throws IOException {
      return Structural.getSection();
    }
  };

  public abstract String getSection() throws IOException;
}
