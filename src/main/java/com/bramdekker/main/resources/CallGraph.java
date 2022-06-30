package com.bramdekker.main.resources;

import com.bramdekker.main.util.CallGraphVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.SimpleGraph;
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
  private SimpleGraph<String, DefaultEdge> moduleGraph;

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
    callGraph.moduleGraph = new SimpleGraph<>(DefaultEdge.class);

    List<String> userDefinedFunctions = HaskellParseTree.getInstance().getFunctionNames();
    for (String functionName : userDefinedFunctions) {
      String moduleName = functionName.split("\\.")[0];
      callGraph.moduleGraph.addVertex(moduleName);
      callGraph.graph.addVertex(functionName);
    }

    CallGraphVisitor callGraphVisitor = new CallGraphVisitor(userDefinedFunctions, callGraph.graph);
    for (Map.Entry<String, ParseTree> entry :
        HaskellParseTree.getInstance().getTreeDict().entrySet()) {
      callGraphVisitor.visit(entry.getValue());
    }

    for (DefaultEdge e : callGraph.graph.edgeSet()) {
      String[] vertexNames = e.toString().replaceAll("[(\\[\\])]", "").split(" : ");
      String source = vertexNames[0].split("\\.")[0];
      String target = vertexNames[1].split("\\.")[0];
      if (!source.equals(target)
          && callGraph.moduleGraph.containsVertex(source)
          && callGraph.moduleGraph.containsVertex(target)) {
        callGraph.moduleGraph.addEdge(source, target);
      }
    }

    return callGraph;
  }

  /**
   * Getter for the directed callgraph on functions.
   *
   * @return DirectedPseudoGraph representing the function callgraph.
   */
  public DirectedPseudograph<String, DefaultEdge> getGraph() {
    return graph;
  }

  /**
   * Getter for the directed callgraph on modules.
   *
   * @return SimpleDirectedGraph representing the module callgraph.
   */
  public SimpleGraph<String, DefaultEdge> getModuleGraph() {
    return moduleGraph;
  }
}
