package com.bramdekker.main.configuration;

import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.metrics.MetricType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationTest {

  @Test
  void constructorFailsWhenUnknownFlagIsUsed() {
    String wrongFlag = "-w";
    String dirName = "/just/some/test/directory";
    String[] args = new String[] {wrongFlag, dirName};

    assertThrows(InvalidCommandException.class, () -> new Configuration(args));
  }

  @Test
  void getDirectoryReturnsLastCommandLineArgument() throws InvalidCommandException {
    String dirName = "/just/some/test/directory";
    String[] args = new String[] {dirName};

    assertEquals(dirName, new Configuration(args).getPathname());
  }

  @Test
  void getDirectoryReturnsLastCommandLineArgumentMultipleFlags() throws InvalidCommandException {
    String dirName = "/just/some/test/directory";
    String[] args = new String[] {"-s", "-rc", dirName};

    assertEquals(dirName, new Configuration(args).getPathname());
  }

  @Test
  void getMetricsReturnsAllMetricsWhenNoFlagsSpecified() throws InvalidCommandException {
    assertEquals(
        Arrays.asList(MetricType.values()),
        new Configuration(new String[] {"test-dir"}).getMetrics());
  }

  @Test
  void getMetricsReturnsOnlySpecifiedMetrics() throws InvalidCommandException {
    assertEquals(
        List.of(MetricType.RECURSION),
        new Configuration(new String[] {"-rc", "test-dir"}).getMetrics());
  }
}
