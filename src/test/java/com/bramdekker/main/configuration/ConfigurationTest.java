package com.bramdekker.main.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bramdekker.main.exceptions.InvalidCommandException;
import com.bramdekker.main.metrics.MetricType;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

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

    assertEquals(dirName, new Configuration(args).getDirectory());
  }

  @Test
  void getDirectoryReturnsLastCommandLineArgumentMultipleFlags() throws InvalidCommandException {
    String dirName = "/just/some/test/directory";
    String[] args = new String[] {"-s", "-r", dirName};

    assertEquals(dirName, new Configuration(args).getDirectory());
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
        new Configuration(new String[] {"-r", "test-dir"}).getMetrics());
  }
}
