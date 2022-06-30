package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.CallGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;

// Strongly connected component for each function
// Indegree for each function
// Outdegree for each function
// Arc-to-node ratio, depth and width oo subgraph of function
/** Collection of methods that determine callgraph metrics. */
public class Callgraph {
  private static final List<CallgraphMetric> dataPerFunction = new ArrayList<>();
  private static long numFunctions;
  private static long internalReuse = 0;
  private static double avgScss = 0.0;
  private static long maxScss = 0;
  private static String maxScssName = "";
  private static double avgIndegree = 0.0;
  private static long maxIndegree = 0;
  private static String maxIndegreeName = "";
  private static double avgOutdegree = 0.0;
  private static long maxOutdegree = 0;
  private static String maxOutdegreeName = "";

  /**
   * Generate the section for callgraph metrics.
   *
   * @return a report section containing information about callgraph metrics as String.
   */
  public static String getSection() throws IOException {
    collectFunctionData();
    calculateMetrics();

    StringBuilder callgraphSection = new StringBuilder("Callgraph metrics: \n");

    callgraphSection.append(getMetricString("Internal reuse", internalReuse));
    callgraphSection.append(
        getMetricString("Average strongly connected component size (nodes)", avgScss));
    callgraphSection.append(getMetricString("Maximum strongly connected component size", maxScss));
    callgraphSection.append(
        getMetricString("Maximum strongly connected component size function name", maxScssName));
    callgraphSection.append(getMetricString("Average indegree", avgIndegree));
    callgraphSection.append(getMetricString("Maximum indegree", maxIndegree));
    callgraphSection.append(getMetricString("Maximum indegree function name", maxIndegreeName));
    callgraphSection.append(getMetricString("Average outdegree", avgOutdegree));
    callgraphSection.append(getMetricString("Maximum outdegree", maxOutdegree));
    callgraphSection.append(getMetricString("Maximum outdegree function name", maxOutdegreeName));

    return callgraphSection.toString();
  }

  /**
   * Collect all data per function needed to calculate metrics.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void collectFunctionData() throws IOException {
    DirectedPseudograph<String, DefaultEdge> callgraph = CallGraph.getInstance().getGraph();
    numFunctions = callgraph.vertexSet().size();

    StrongConnectivityAlgorithm<String, DefaultEdge> scAlg =
        new KosarajuStrongConnectivityInspector<>(callgraph);
    List<Graph<String, DefaultEdge>> stronglyConnectedSubgraphs =
        scAlg.getStronglyConnectedComponents();

    for (Graph<String, DefaultEdge> stronglyConnectedSubgraph : stronglyConnectedSubgraphs) {
      long curScss = stronglyConnectedSubgraph.vertexSet().size();

      for (String v : stronglyConnectedSubgraph.vertexSet()) {
        dataPerFunction.add(
            new CallgraphMetric(v, curScss, callgraph.inDegreeOf(v), callgraph.outDegreeOf(v)));
      }
    }
  }

  /**
   * Calculate all callgraph metrics and store them as static variables.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void calculateMetrics() throws IOException {
    SimpleGraph<String, DefaultEdge> moduleGraph = CallGraph.getInstance().getModuleGraph();
    internalReuse = moduleGraph.edgeSet().size() - moduleGraph.vertexSet().size() + 1;

    for (CallgraphMetric m : dataPerFunction) {
      avgScss += m.sccs;
      avgIndegree += m.indegree;
      avgOutdegree += m.outdegree;

      if (m.sccs > maxScss) {
        maxScss = m.sccs;
        maxScssName = m.name;
      }

      if (m.indegree > maxIndegree) {
        maxIndegree = m.indegree;
        maxIndegreeName = m.name;
      }

      if (m.outdegree > maxOutdegree) {
        maxOutdegree = m.outdegree;
        maxOutdegreeName = m.name;
      }
    }

    avgScss /= numFunctions;
    avgIndegree /= numFunctions;
    avgOutdegree /= numFunctions;
  }
}
