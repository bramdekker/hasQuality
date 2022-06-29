package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.CallGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;

/** Collection of methods that determine recursion metrics. */
public class Recursion {
  private static long numRecursiveFunctions = 0;
  private static long numTrivialRecursiveFunctions = 0;
  private static long numNonTrivialRecursiveFunctions = 0;
  private static long longestNonTrivialPath = 0;
  private static String longestNonTrivialPathName = "";
  private static double recursiveFunctionRatio = 0.0;
  private static long maxRecursiveFunctions = 0;
  private static String maxRecursiveFunctionsModuleName = "";

  /**
   * Generate the section for recursion metrics.
   *
   * @return a report section containing information about recursion metrics as String.
   */
  public static String getSection() throws IOException {
    calculateMetrics();

    StringBuilder recursionSection = new StringBuilder("Recursion metrics:\n");

    recursionSection.append(
        getMetricString("Number of recursive functions", numRecursiveFunctions));
    recursionSection.append(
        getMetricString("Number of trivial recursive functions", numTrivialRecursiveFunctions));
    recursionSection.append(
        getMetricString(
            "Number of non-trivial recursive functions", numNonTrivialRecursiveFunctions));
    recursionSection.append(
        getMetricString("Longest non-trivial call chain", longestNonTrivialPath));
    recursionSection.append(
        getMetricString(
            "Longest non-trivial call chain function names", longestNonTrivialPathName));
    recursionSection.append(
        getMetricString("Ratio recursive/non-recursive functions", recursiveFunctionRatio));
    recursionSection.append(
        getMetricString("Maximum number of recursive functions", maxRecursiveFunctions));
    recursionSection.append(
        getMetricString(
            "Maximum number of recursive functions module name", maxRecursiveFunctionsModuleName));

    return recursionSection.toString();
  }

  /**
   * Calculate all recursion metrics and store them as static variables.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void calculateMetrics() throws IOException {
    DirectedPseudograph<String, DefaultEdge> callgraph = CallGraph.getInstance().getGraph();
    int totalNumFunctions = callgraph.vertexSet().size();

    Map<String, Integer> moduleRecursiveDict = new HashMap<>();

    StrongConnectivityAlgorithm<String, DefaultEdge> scAlg =
        new KosarajuStrongConnectivityInspector<>(callgraph);
    List<Graph<String, DefaultEdge>> stronglyConnectedSubgraphs =
        scAlg.getStronglyConnectedComponents();

    for (Graph<String, DefaultEdge> stronglyConnectedSubgraph : stronglyConnectedSubgraphs) {
      if (stronglyConnectedSubgraph.edgeSet().size() > 0) {
        numRecursiveFunctions++;
        int subgraphSize = stronglyConnectedSubgraph.vertexSet().size();

        for (String v : stronglyConnectedSubgraph.vertexSet()) {
          String moduleName = v.split("\\.")[0];
          if (moduleRecursiveDict.containsKey(moduleName)) {
            int prevNum = moduleRecursiveDict.get(moduleName);
            moduleRecursiveDict.put(moduleName, prevNum + 1);
          } else {
            moduleRecursiveDict.put(moduleName, 1);
          }
        }

        if (subgraphSize == 1) {
          numTrivialRecursiveFunctions++;
        } else {
          numNonTrivialRecursiveFunctions++;
        }

        if (subgraphSize > longestNonTrivialPath && subgraphSize > 1) {
          longestNonTrivialPath = subgraphSize;
          longestNonTrivialPathName =
              String.valueOf(
                      stronglyConnectedSubgraph.vertexSet().stream().reduce((s, a) -> s + ", " + a))
                  .replace("Optional", "");
        }
      }
    }

    recursiveFunctionRatio = (double) numRecursiveFunctions / totalNumFunctions;
    for (Map.Entry<String, Integer> e : moduleRecursiveDict.entrySet()) {
      if (e.getValue() > maxRecursiveFunctions) {
        maxRecursiveFunctions = e.getValue();
        maxRecursiveFunctionsModuleName = e.getKey();
      }
    }
  }
}
