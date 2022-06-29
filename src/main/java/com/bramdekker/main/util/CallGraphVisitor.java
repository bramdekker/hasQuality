package com.bramdekker.main.util;

import antlr.HaskellParser;
import antlr.HaskellParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallGraphVisitor extends HaskellParserBaseVisitor<Void> {
  private final List<String> userDefinedFunctions;
  private List<String> internalFunctions;
  private String module;
  private final LeafVisitor leafVisitor = new LeafVisitor();
  private final DirectedPseudograph<String, DefaultEdge> callGraph;
  private Map<String, String> importedFunctions;
  private String currentFunction;
  // TODO: callgraph seems to be working correctly!!!
  // TODO: now implemented/use algorithms on it to calculate structural metrics
  public CallGraphVisitor(
      List<String> userDefinedFunctions, DirectedPseudograph<String, DefaultEdge> callGraph) {
    this.userDefinedFunctions = userDefinedFunctions;
    this.callGraph = callGraph;
    this.currentFunction = "";
    this.importedFunctions = new HashMap<>();
  }

  @Override
  public Void visitModule(HaskellParser.ModuleContext ctx) {
    this.currentFunction = "";
    this.importedFunctions = new HashMap<>();
    return super.visitModule(ctx);
  }

  @Override
  public Void visitImpdecl(HaskellParser.ImpdeclContext ctx) {
    String renamed;
    String originalModule = "";
    boolean afterRenaming = false;
    boolean qualified = false;
    Map<String, String> curImportedFunctions = new HashMap<>();

    for (int i = 0; i < ctx.getChildCount(); i++) {
      ParseTree curChild = ctx.getChild(i);

      if (curChild instanceof TerminalNode && curChild.getText().equals("qualified")) {
        qualified = true;
      }

      if (curChild instanceof HaskellParser.ModidContext) {
        if (afterRenaming) {
          renamed = getLeftMostChild(curChild).getText();
          String finalOriginalModule = originalModule;
          String finalRenamed = renamed;

          if (qualified) {
            curImportedFunctions.replaceAll((k, v) -> v.replace(finalOriginalModule, finalRenamed));
          } else {
              String finalRenamed1 = renamed;
            curImportedFunctions.replaceAll((k, v) -> finalRenamed1 + "." + v);
          }
        } else {
          originalModule = getLeftMostChild(curChild).getText();
          String modulePrefix = originalModule + ".";
          for (String func : userDefinedFunctions) {
            if (func.startsWith(modulePrefix)) {
              curImportedFunctions.put(func, func);
            }

          }
          if (!qualified) {
            curImportedFunctions.replaceAll((k, v) -> v.substring(modulePrefix.length()));
          }
        }
      }

      if (curChild instanceof TerminalNode && curChild.getText().equals("as")) {
        afterRenaming = true;
      }

      if (curChild instanceof HaskellParser.ImpspecContext) {
        List<TerminalNode> leaves = leafVisitor.visit(curChild);
        List<String> funcs =
            leaves.stream()
                .map(ParseTree::getText)
                .filter(f -> !(f.equals(",") || f.equals("(") || f.equals(")") || f.equals("hiding")))
                .toList();

        if (curChild.getChild(0).getText().equals("hiding")) {
          for (String name: funcs) {
            curImportedFunctions.remove(originalModule + "." + name);
          }
        } else {
          Map<String, String> actualImported = new HashMap<>();
          for (String name: funcs) {
            actualImported.put(originalModule + "." + name, curImportedFunctions.get(originalModule + "." + name));
          }
          curImportedFunctions = actualImported;
        }
      }
    }

    importedFunctions.putAll(curImportedFunctions);

    return null;
  }

  @Override
  public Void visitModule_content(HaskellParser.Module_contentContext ctx) {
    StringBuilder currentModule = new StringBuilder();
    ParseTree modid = ctx.getChild(1);
    for (int i = 0; i < modid.getChildCount(); i++) {
      currentModule.append(modid.getChild(i).getText());
    }
    module = currentModule.toString();
    this.internalFunctions =
        userDefinedFunctions.stream()
            .filter(f -> f.startsWith(module + "."))
            .map(f -> f.substring(module.length() + 1))
            .toList();
    return super.visitModule_content(ctx);
  }

  @Override
  public Void visitDecl_no_th(HaskellParser.Decl_no_thContext ctx) {
    if (!(ctx.getChild(0) instanceof HaskellParser.SigdeclContext)) {
      List<TerminalNode> leaves = leafVisitor.visit(ctx);
      currentFunction = leaves.remove(0).getText();
      return super.visitDecl_no_th(ctx);
    }

    return null;
  }

  @Override
  public Void visitQvarid(HaskellParser.QvaridContext ctx) {
    if (ctx.getChildCount() == 3) {
      List<TerminalNode> leaves = leafVisitor.visit(ctx);
      String operatorName = String.join("", leaves.stream().map(ParseTree::getText).toList());
      checkIfImportedFunction(operatorName);
      return null;
    }
    return super.visitQvarid(ctx);
  }

  @Override
  public Void visitQvarsym(HaskellParser.QvarsymContext ctx) {
    if (ctx.getChildCount() == 3) {
      List<TerminalNode> leaves = leafVisitor.visit(ctx);
      String operatorName = String.join("", leaves.stream().map(ParseTree::getText).toList());
      checkIfImportedFunction(operatorName);
      return null;
    }
    return super.visitQvarsym(ctx);
  }

  @Override
  public Void visitVarid(HaskellParser.VaridContext ctx) {
    String operatorName = ctx.getChild(0).getText();
    if (operatorName.equals(currentFunction) && onRightHandSide(ctx)) {
      // Add edge from currentFunction to currentFunction: recursion!
      if (callGraph.containsVertex(getFunctionName())) {
        callGraph.addEdge(getFunctionName(), getFunctionName());
      }
    } else if (operatorName.equals(currentFunction)) {
      return null;
    } else if (this.internalFunctions.contains(operatorName)) {
      // Add edge from currentFunction to called function: intra-module function call!
      if (callGraph.containsVertex(getFunctionName()) && callGraph.containsVertex(module + "." + operatorName))
        callGraph.addEdge(getFunctionName(), module + "." + operatorName);
    } else {
      checkIfImportedFunction(operatorName);
    }


    return null;
  }

  private void checkIfImportedFunction(String operatorName) {
    if (importedFunctions.containsValue(operatorName)) {
      String targetNodeName = "";

      for (Map.Entry<String, String> e : importedFunctions.entrySet()) {
        if (e.getValue() != null && e.getValue().equals(operatorName)) {
          targetNodeName = e.getKey();
        }
      }

      if (callGraph.containsVertex(getFunctionName()) && callGraph.containsVertex(targetNodeName)) {
        callGraph.addEdge(getFunctionName(), targetNodeName);
      }
    }
  }

  /**
   * Check if the node is a child of a rhs node.
   *
   * @param node the node to be checked.
   * @return true if it is a child of a rhs node; false otherwise.
   */
  private boolean onRightHandSide(ParseTree node) {
    while (node != null) {
      if (node instanceof HaskellParser.Exp10pContext || node instanceof HaskellParser.RhsContext) {
        return true;
      }

      node = node.getParent();
    }

    return false;
  }

  /**
   * Get the left most child from node using depth-first search.
   *
   * @param node the start node.
   * @return the left most child of the node.
   */
  private ParseTree getLeftMostChild(ParseTree node) {
    if (node.getChildCount() > 0) {
      return getLeftMostChild(node.getChild(0));
    }

    return node;
  }

  /**
   * Get the name of the function potentially prepended with a module name.
   *
   * @return the most specific name for the function.
   */
  private String getFunctionName() {
    if (module.isEmpty()) {
      return currentFunction;
    }

    return module + "." + currentFunction;
  }
}
