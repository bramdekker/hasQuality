package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

// callgraph-project
// internal reuse = 3 - 4 + 1 = 0
// Average sccs (nodes) = 1.22
// Max scss = 3 (Generic.or', Individual.iOr', Puzzles.pOr')
// Average indegree = 14 / 27 = 0.52
// Max indegree = 3 (Generic.length')
// Avg outdegree = 0.48
// Max outdegree = 3 (Puzzles.isPalindrome)
@Disabled
class CallgraphTest {
  static String pathToTestResources;
  static String callgraphSection;

  @BeforeAll
  public static void setup() throws IOException {
    Path resourceDirectory = Paths.get("src", "test", "resources");
    pathToTestResources = resourceDirectory.toFile().getAbsolutePath();
    FileList.init(pathToTestResources + "/callgraph-project");
    callgraphSection = Callgraph.getSection();
    System.out.println(callgraphSection);
  }

  @Test
  void internalReuseMeasureIsCorrect() {
    assertTrue(callgraphSection.contains("Internal reuse: 0"));
  }

  @Test
  void avgSccsMeasureIsCorrect() {
    assertTrue(
        callgraphSection.contains("Average strongly connected component size (nodes): 1.22"));
  }

  @Test
  void maxSccsMeasureIsCorrect() {
    assertTrue(callgraphSection.contains("Maximum strongly connected component size: 3"));
  }

  @Test
  void maxSccsNameMeasureIsCorrect() {
    String individualOrRegex =
        "(?s).*Maximum strongly connected component size function name:.*Individual.iOr'.*$";

    assertTrue(
        callgraphSection.matches(
                "(?s).*Maximum strongly connected component size function name:.*Generic.or'.*$")
            || callgraphSection.matches(individualOrRegex)
            || callgraphSection.matches(
                "(?s).*Maximum strongly connected component size function name:.*Puzzles.pOr'.*$"));
  }

  @Test
  void avgIndegreeMeasureIsCorrect() {
    assertTrue(callgraphSection.contains("Average indegree: 0.52"));
  }

  @Test
  void maxIndegreeMeasureIsCorrect() {
    assertTrue(callgraphSection.contains("Maximum indegree: 3"));
  }

  @Test
  void maxIndegreeNameMeasureIsCorrect() {
    assertTrue(
            callgraphSection.matches(
                    "(?s).*Maximum indegree function name:.*Generic.length'.*$"));
  }

  @Test
  void avgOutdegreeMeasureIsCorrect() {
    assertTrue(callgraphSection.contains("Average outdegree: 0.52"));
  }

  @Test
  void maxOutdegreeMeasureIsCorrect() {
    assertTrue(callgraphSection.contains("Maximum outdegree: 3"));
  }

  @Test
  void maxOutdegreeNameMeasureIsCorrect() {
    assertTrue(
        callgraphSection.matches(
            "(?s).*Maximum outdegree function name:.*Puzzles.isPalindrome.*$"));
  }
}
