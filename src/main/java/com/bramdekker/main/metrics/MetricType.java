package com.bramdekker.main.metrics;

/**
 * Enum for all different metric types. The types all have a different section which can be accessed
 * via the getSection method.
 */
public enum MetricType {
  SIZE {
    public String getSection() {
      return Size.getSection();
    }
  },
  RECURSION {
    public String getSection() {
      return Recursion.getSection();
    }
  };

  public abstract String getSection();
}
