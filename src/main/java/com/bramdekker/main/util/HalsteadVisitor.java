package com.bramdekker.main.util;

import antlr.HaskellParser;
import antlr.HaskellParserBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Visitor that can split the tokens into operators and operands.
 */
public class HalsteadVisitor extends HaskellParserBaseVisitor<Void> {
  private static final LeafVisitor leafVisitor = new LeafVisitor();
  private static String currentFunction;
  private static String lastOperator = "";
  private static final List<String> functions = new ArrayList<>();
  private static final List<String> datas = new ArrayList<>();
  private static final List<String> types = new ArrayList<>();
  private static final List<String> ignoredTokens =
      List.of("}", "]", ")", ",", "<EOF>", "SEMI", "VOCURLY", "VCCURLY");

  private static final List<String> possibleTwoCharOperators =
      List.of("!!", "<=", ">=", "==", "!=", "**", "^^", "++", "||");

  private static final List<List<String>> variablesInScope = new ArrayList<>();
  private final Map<String, Integer> operatorMap;
  private final Map<String, Integer> operandMap;

  /**
   * Initialize the operand and operator dictionaries.
   */
  public HalsteadVisitor() {
    this.operatorMap = new HashMap<>();
    this.operandMap = new HashMap<>();
  }

  @Override
  public Void visitTopdecl(HaskellParser.TopdeclContext ctx) {
    // Top-level declaration has new scope.
    variablesInScope.clear();
    return super.visitTopdecl(ctx);
  }

  @Override
  public Void visitTy_decl(HaskellParser.Ty_declContext ctx) {
    if (ctx.getChild(0).getText().equals("type")) {
      types.add(getLeftMostChild(ctx.getChild(1)).getText());
    } else if (ctx.getChild(0).getText().equals("data")) {
      datas.add(getLeftMostChild(ctx.getChild(1)).getText());
    }

    return super.visitTy_decl(ctx);
  }

  @Override
  public Void visitDecl_no_th(HaskellParser.Decl_no_thContext ctx) {
    if (ctx.getChildCount() > 0) {
      // If in where, don't do anything
      if (inWhereClause(ctx)) {
        return super.visitDecl_no_th(ctx);
      }

      ParseTree rootLeftSubtree = getRootLeftSubtree(ctx);
      List<TerminalNode> leaves = leafVisitor.visit(rootLeftSubtree);

      if (ctx.getParent() instanceof HaskellParser.TopdeclContext) {
        TerminalNode functionName = leaves.remove(0);
        currentFunction = functionName.getText();
        if (!functions.contains(functionName.getText())) {
          functions.add(functionName.getText());
        }
      }

      // If right-hand side has a where-clause, add those variables to scope.
      ParseTree connectedWhere = getConnectedWhere(ctx);
      if (connectedWhere != null) {
        System.out.println("Found a connected where");
        List<TerminalNode> whereLeaves = leafVisitor.visit(connectedWhere);
        List<String> whereVariables = new ArrayList<>();
        for (int i = 0; i < whereLeaves.size() - 1; i++) {
          if (whereLeaves.get(i + 1).getText().equals("=")) {
            whereVariables.add(whereLeaves.get(i).getText());
          }
        }

        variablesInScope.add(whereVariables);
      }

      List<String> leavesNoGuards = removeGuards(leavesToStringList(leaves));
      variablesInScope.add(leavesNoGuards);
    }

    return super.visitDecl_no_th(ctx);
  }

  @Override
  public Void visitTerminal(TerminalNode node) {
    boolean setLastOperator = false;
    String text = node.getText();
    if (!(ignoredTokens.contains(text))) {
      if (text.equals(currentFunction) && onRightHandSide(node)) {
        updateOperatorMap(text);
      } else if (text.equals(currentFunction)) {
        updateOperandMap(text);
      } else if (inScope(text)
          || isTypeInSigdecl(node)
          || text.equals("_")
          || (lastOperator.equals("|") && text.equals("otherwise"))
          || isLiteral(node)
          || isType(text)) {
        updateOperandMap(text);
      } else {
        if (text.length() == 1 && lastOperator.isEmpty()) {
          setLastOperator = true;
          lastOperator = text;
        } else if (text.length() == 1 && possibleTwoCharOperators.contains(lastOperator + text)) {
          updateOperatorMap(lastOperator + text);
          lastOperator = "";
        } else {
          updateOperatorMap(text);
        }
      }

      if (!setLastOperator && !lastOperator.isEmpty()) {
        updateOperatorMap(lastOperator);
        lastOperator = "";
      }
    }

    return null;
  }

  @Override
  public Void visitAexp(HaskellParser.AexpContext ctx) {
    if (ctx.getChildCount() > 1 && ctx.getChild(0).getText().equals("\\")) {
      // The second child is apats.
      List<TerminalNode> lambdaParameters = leafVisitor.visit(ctx.getChild(1));
      variablesInScope.add(leavesToStringList(lambdaParameters));
    }

    super.visitAexp(ctx);

    if (ctx.getChildCount() > 1 && ctx.getChild(0).getText().equals("\\")) {
      variablesInScope.remove(variablesInScope.size() - 1);
    }

    return null;
  }

  /**
   * Remove all guards from a list of leaf nodes.
   *
   * @param leaves List of Strings representing leaf nodes.
   * @return all leaf nodes that come before the first guard (|).
   */
  private static List<String> removeGuards(List<String> leaves) {
    int guardStart = leaves.indexOf("|");
    if (guardStart > -1) {
      return leaves.subList(0, guardStart);
    }

    return leaves;
  }

  /**
   * Get the connected where-clause for a node, if it has one.
   *
   * @param node the node to be checked.
   * @return the wherebinds node if it has one; null otherwise.
   */
  private static ParseTree getConnectedWhere(ParseTree node) {
    for (int i = 0; i < node.getChildCount(); i++) {
      if (node.getChild(i) instanceof HaskellParser.RhsContext) {
        ParseTree rhs = node.getChild(i);
        for (int j = 0; j < rhs.getChildCount(); j++) {
          if (rhs.getChild(j) instanceof HaskellParser.WherebindsContext) {
            return rhs.getChild(j);
          }
        }
      }
    }

    return null;
  }

  /**
   * Check if the node is a child of a rhs node.
   *
   * @param node the node to be checked.
   * @return true if it is a child of a rhs node; false otherwise.
   */
  private static boolean onRightHandSide(ParseTree node) {
    while (node != null) {
      if (node instanceof HaskellParser.RhsContext) {
        return true;
      }

      node = node.getParent();
    }

    return false;
  }

  /**
   * Check if a variable is in scope.
   *
   * @param variable String representing the variable to be checked.
   * @return true if it is in scope; false otherwise.
   */
  private static boolean inScope(String variable) {
    for (List<String> scope : variablesInScope) {
      if (scope.contains(variable)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Check if the node is in a where-clause.
   *
   * @param node the node to be checked.
   * @return true if the node has a wherebinds as parent; false otherwise.
   */
  private static boolean inWhereClause(ParseTree node) {
    while (node != null) {
      if (node instanceof HaskellParser.WherebindsContext) {
        return true;
      }

      node = node.getParent();
    }

    return false;
  }

  /**
   * Check if the node is a type in a signature declaration.
   *
   * @param node the node to be checked.
   * @return true if the node has a sigdecl node as parent and starts with lowercase letter;
   *         false otherwise.
   */
  private static boolean isTypeInSigdecl(ParseTree node) {
    if (node.getText().matches("^[a-zA-Z]*$")) {
      while (node != null) {
        if (node instanceof HaskellParser.SigdeclContext) {
          return true;
        }

        node = node.getParent();
      }
    }

    return false;
  }

  /**
   * Check if the node is a literal.
   *
   * @param node the node to be checked.
   * @return true is the grandparent of the node is a literal; false otherwise.
   */
  private static boolean isLiteral(ParseTree node) {
    return node.getParent().getParent() instanceof HaskellParser.LiteralContext;
  }

  /**
   * Check if the variable is a type.
   *
   * @param variable the variable to be checked.
   * @retur true if the variable starts with uppercase; false otherwise.
   */
  private static boolean isType(String variable) {
    return Character.isUpperCase(variable.charAt(0));
  }

  /**
   * Convert a List of TerminalNode's into a List of String's using the getText()-method.
   *
   * @param nodes the List of TerminalNode's to be converted.
   * @return List of Strings containing the text for every TerminalNode.
   */
  private static List<String> leavesToStringList(List<TerminalNode> nodes) {
    return nodes.stream().map(ParseTree::getText).toList();
  }

  /**
   * Get the root of the first left subtree with multiple children.
   *
   * @param node the start node.
   * @return the root of the first left subtree with multiple children.
   * @throws RuntimeException if the node does not have any children.
   */
  private static ParseTree getRootLeftSubtree(ParseTree node) {
    if (node.getChildCount() == 1) {
      return getRootLeftSubtree(node.getChild(0));
    } else if (node.getChildCount() == 0) {
      throw new RuntimeException("Node does not have a child!");
    }

    return node.getChild(0);
  }

  /**
   * Get the left most child from node using depth-first search.
   *
   * @param node the start node.
   * @return the left most child of the node.
   */
  private static ParseTree getLeftMostChild(ParseTree node) {
    if (node.getChildCount() > 0) {
      return getLeftMostChild(node.getChild(0));
    }

    return node;
  }

  /**
   * Getter for the functions field.
   *
   * @return List of Strings with all function defined in the parse tree.
   */
  public static List<String> getFunctions() {
    return functions;
  }

  /**
   * Use the listener to walk the parse tree and populate the dictionaries.
   *
   * @param tree ParseTree generated by antlr.
   */
  public void createHalsteadMaps(ParseTree tree) {
    this.visit(tree);
  }

  /**
   * Calculates the halstead length based on the dictionaries.
   *
   * @return long representing the sum of total occurrences of operands and operators.
   */
  public long getHalsteadLength() {
    long length = 0;
    //            System.out.println("Operands: ");
    //            for (Map.Entry<String, Integer> entry : this.operandMap.entrySet())
    //              System.out.println(entry.getKey() + " : " + entry.getValue());
    for (int number : this.operandMap.values()) {
      length += number;
    }
    //            System.out.println("Operators: ");
    //            for (Map.Entry<String, Integer> entry : this.operatorMap.entrySet())
    //              System.out.println(entry.getKey() + " : " + entry.getValue());
    for (int number : this.operatorMap.values()) {
      length += number;
    }
    System.out.println();
    return length;
  }

  /**
   * Calculates the halstead vocabulary based on the dictionaries.
   *
   * @return long representing the sum of unique occurrences of operands and operators.
   */
  public long getHalsteadVocabulary() {
    return operandMap.size() + operatorMap.size();
  }

  /**
   * Update the operand dictionary when an occurrence of key is found.
   *
   * @param key String representing the operand that is found.
   */
  private void updateOperandMap(String key) {
    if (operandMap.containsKey(key)) {
      operandMap.put(key, operandMap.get(key) + 1);
    } else {
      operandMap.put(key, 1);
    }
  }

  /**
   * Update the operator dictionary when an occurrence of key is found.
   *
   * @param key String representing the operator that is found.
   */
  private void updateOperatorMap(String key) {
    if (operatorMap.containsKey(key)) {
      operatorMap.put(key, operatorMap.get(key) + 1);
    } else {
      operatorMap.put(key, 1);
    }
  }
}
