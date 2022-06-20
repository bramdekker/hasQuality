package com.bramdekker.main.resources;

import java.io.FileNotFoundException;
import java.io.IOException;

// TODO: use jgrapht for a directed graph

public class ModuleDependencyGraph {
    private static ModuleDependencyGraph instance;
    private static DirectedGraph<String, DefaultEdge> graph;

    /** Private constructor to make it singleton. */
    private ModuleDependencyGraph() {}

    /**
     * Get the instance of ModuleDependencyGraph. If it does not exist yet, generate one.
     *
     * @return instance of ModuleDependencyGraph with the graph constructed.
     */
    public static ModuleDependencyGraph getInstance() {
        if (instance == null) {
            instance = generateInstance();
        }

        return instance;
    }

    private static ModuleDependencyGraph generateInstance() {
        ModuleDependencyGraph dependencyGraph = new ModuleDependencyGraph();

        // TODO: use the treeDict from HaskellParseTree, loop over the import statements and add edges/nodes to the graph

        return dependencyGraph;
    }
}

