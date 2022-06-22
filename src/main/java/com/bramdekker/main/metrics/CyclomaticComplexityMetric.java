package com.bramdekker.main.metrics;

/**
 * Class that encapsulates all info needed for cyclomatic complexity of a function and the number
 * of operators and operadns of it.
 */
public class CyclomaticComplexityMetric {
  private long numBranches = 0;
  private long numOperators = 0;
  private long numOperands = 0;

  public CyclomaticComplexityMetric() {}

  public CyclomaticComplexityMetric incrementNumBranches() {
    this.numBranches++;
    return this;
  }

  public CyclomaticComplexityMetric decrementNumBranches() {
    this.numBranches--;
    return this;
  }

  public CyclomaticComplexityMetric incrementNumOperators() {
    this.numOperators++;
    return this;
  }

  public CyclomaticComplexityMetric incrementNumOperands() {
    this.numOperands++;
    return this;
  }

  public long getNumBranches() {
    return numBranches;
  }

  public long getNumOperators() {
    return numOperators;
  }

  public long getNumOperands() {
    return numOperands;
  }
}
