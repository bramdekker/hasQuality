package com.bramdekker.main.report;

import com.bramdekker.main.configuration.Configuration;
import com.bramdekker.main.metrics.MetricType;
import com.bramdekker.main.resources.FileList;

/**
 * Class that assembles the final report. It contains sections with metrics measuring the same
 * attribute, e.g. size, recursion, patterns.
 */
public class Report {
  private String report;
  private Configuration config;

  public Report() {
    this.report = "";
  }

  /**
   * Constructor that takes as argument a Configuration instance. It also initialized the report
   * variable and initializes resources.
   *
   * @param config an instance of Configuration.
   */
  public Report(Configuration config) {
    this.report = "";
    this.config = config;
    initializeResources();
  }

  /** Initialize the resources with information they need to generate the resources. */
  private void initializeResources() {
    FileList.init(config.getDirectory());
  }

  /** Prints out the report in sections. */
  public void display() {
    System.out.println(String.format("Report for Haskell project %s\n", config.getDirectory()));
    for (MetricType metric : config.getMetrics()) {
      System.out.println(metric.getSection());
      System.out.println();
    }
  }

  /**
   * Appends a section to the report.
   *
   * @param section the section to be appended as String.
   */
  public void appendSection(String section) {
    this.report += section;
  }

  /**
   * Prepend a section in front of the report.
   *
   * @param section the section to be prepended as String.
   */
  public void prependSection(String section) {
    this.report = section + this.report;
  }

  /**
   * Getter for the report variable.
   *
   * @return String representing the report.
   */
  public String getReport() {
    return this.report;
  }
}
