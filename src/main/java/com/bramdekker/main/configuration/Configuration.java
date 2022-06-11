package com.bramdekker.main.configuration;

import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.metrics.MetricType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bramdekker.main.metrics.MetricType.*;

/**
 * Sets up the configuration for the report and metrics based on the flags specified in the command.
 */
public class Configuration {
  private static final List<MetricType> allMetrics = Arrays.asList(MetricType.values());
  private final List<MetricType> metrics = new ArrayList<>();
  private final String pathname;

  /**
   * Constructor that takes all command line arguments as parameter. It initializes the directory
   * and metrics variables.
   *
   * @param args String array containing all command line arguments.
   * @throws InvalidCommandException when an unknown flag is entered as argument.
   */
  public Configuration(String[] args) throws InvalidCommandException {
    // Set the directory. We know this is the last element in the arguments list since it passed the
    // validator.
    this.pathname = args[args.length - 1];

    // Fill the list with metrics according to the flags set.
    for (int i = 0; i < args.length - 1; i++) {
      switch (args[i]) {
        case "-s" -> this.metrics.add(SIZE);
        case "-r" -> this.metrics.add(RECURSION);
        case "-p" -> this.metrics.add(PATTERNS);
        case "-u" -> this.metrics.add(USABILITY);
        case "-h" -> this.metrics.add(HALSTEAD);
        default -> throw new InvalidCommandException("Unknown flag used!");
      }
    }

    // If no flags are set, then just calculate all metrics.
    if (metrics.size() == 0) {
      this.metrics.addAll(allMetrics);
    }
  }

  /**
   * Getter for the directory variable.
   *
   * @return a String specifying the directory.
   */
  public String getPathname() {
    return this.pathname;
  }

  /**
   * Getter for the metrics variable.
   *
   * @return a List of MetricTypes that must be calculated for the report.
   */
  public List<MetricType> getMetrics() {
    return metrics;
  }
}
