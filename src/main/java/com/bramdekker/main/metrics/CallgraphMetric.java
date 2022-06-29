package com.bramdekker.main.metrics;

/** Encapsulates Callgraph data for a specific file. */
public class CallgraphMetric {
  String name;
  public long sccs;
  public long indegree;
  public long outdegree;

  /**
   * Constructor that sets all data accordingly.
   *
   * @param name the name of the function.
   * @param scss the size of the strongly connected component (nodes).
   * @param indegree the indegree of the function.
   * @param outdegree the outdegree of the function.
   */
  public CallgraphMetric(String name, long scss, long indegree, long outdegree) {
    this.name = name;
    this.sccs = scss;
    this.indegree = indegree;
    this.outdegree = outdegree;
  }
}
