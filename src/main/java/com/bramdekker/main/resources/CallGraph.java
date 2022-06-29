package com.bramdekker.main.resources;

import com.bramdekker.main.util.CallGraphVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.DepthFirstIterator;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A singleton class that contains functionality to generate a callgraph on all the functions in the
 * project.
 */
public class CallGraph {
  private static CallGraph instance;
  private DirectedPseudograph<String, DefaultEdge> graph;

  /** Private constructor to make it singleton. */
  private CallGraph() {}

  /**
   * Get the instance of CallGraph. If it does not exist yet, generate one.
   *
   * @return instance of CallGraph with the graph constructed.
   */
  public static CallGraph getInstance() throws IOException {
    if (instance == null) {
      instance = generateInstance();
    }

    return instance;
  }

  /**
   * Generate an instance of the CallGraph singleton by generating the callgraph.
   *
   * @return an instance of CallGraph with the callgraph populated.
   * @throws IOException if HaskellParseTree finds a File that doesn't exist.
   */
  private static CallGraph generateInstance() throws IOException {
    CallGraph callGraph = new CallGraph();
    callGraph.graph = new DirectedPseudograph<>(DefaultEdge.class);

    List<String> userDefinedFunctions = HaskellParseTree.getInstance().getFunctionNames();
    for (String functionName : userDefinedFunctions) {
      callGraph.graph.addVertex(functionName);
    }

    CallGraphVisitor callGraphVisitor = new CallGraphVisitor(userDefinedFunctions, callGraph.graph);
    for (Map.Entry<String, ParseTree> entry :
        HaskellParseTree.getInstance().getTreeDict().entrySet()) {
      callGraphVisitor.visit(entry.getValue());
    }

    //        // Print out the graph to be sure it's really complete
    //        Iterator<String> iter = new DepthFirstIterator<>(callGraph.graph);
    //        while (iter.hasNext()) {
    //            String vertex = iter.next();
    //            System.out
    //                    .println(
    //                            "Vertex " + vertex + " is connected to: "
    //                                    + callGraph.graph.edgesOf(vertex).toString());
    //        }

    return callGraph;
  }

  /**
   * Getter for the directed callgraph.
   *
   * @return DirectedPseudoGraph representing the function callgraph.
   */
  public DirectedPseudograph<String, DefaultEdge> getGraph() {
    return graph;
  }
}
