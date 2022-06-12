package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.HaskellParseTree;
import com.bramdekker.main.util.LeafVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.util.*;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;

/** Collection of methods that determine pattern metrics. */
public class Patterns {
  private static final List<PatternMetric> dataPerPattern = new ArrayList<>();
  private static double avgNumberOfVariables = 0;
  private static double avgNumberOfConstructors = 0;
  private static double avgNumberOfWildcards = 0;
  private static double avgSumDepthOfNesting = 0;
  private static long maxSumDepthOfNesting = 0;
  private static String maxSumDepthOfNestingName = "";
  private static double avgDepthOfNesting = 0;
  private static long maxDepthOfNesting = 0;
  private static String maxDepthOfNestingName = "";
  private static double avgPatternSize = 0; // (nodes in parse tree)
  private static long maxPatternSize = 0;
  private static String maxPatternSizeName = "";

  /**
   * Get the section on pattern metrics.
   *
   * @return a String containing the section with pattern metrics.
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  public static String getSection() throws IOException {
    collectFileData();
    calculateMetrics();

    StringBuilder patternSection = new StringBuilder("Pattern metrics:\n");

    patternSection.append(getMetricString("Average number of variables", avgNumberOfVariables));
    patternSection.append(
        getMetricString("Average number of constructors", avgNumberOfConstructors));
    patternSection.append(getMetricString("Average number of wildcards", avgNumberOfWildcards));
    patternSection.append(getMetricString("Average depth of nesting", avgDepthOfNesting));
    patternSection.append(getMetricString("Maximum depth of nesting", maxDepthOfNesting));
    patternSection.append(
        getMetricString("Maximum depth of nesting function name", maxDepthOfNestingName));
    patternSection.append(getMetricString("Average sum of depth of nesting", avgSumDepthOfNesting));
    patternSection.append(getMetricString("Maximum sum of depth of nesting", maxSumDepthOfNesting));
    patternSection.append(
        getMetricString("Maximum sum of depth of nesting function name", maxSumDepthOfNestingName));
    patternSection.append(getMetricString("Average pattern size (nodes)", avgPatternSize));
    patternSection.append(getMetricString("Maximum pattern size", maxPatternSize));
    patternSection.append(
        getMetricString("Maximum pattern size function name", maxPatternSizeName));

    return patternSection.toString();
  }

  // TODO: refactor duplicated lines!!!
  private static void collectFileData() throws IOException {
    for (Map.Entry<String, ParseTree> entry :
        HaskellParseTree.getInstance().getPatternDict().entrySet()) {
      boolean inGuard = true;
      boolean lastTokenWasEquals = false;
      boolean lastTokenWasPipe = false;
      LeafVisitor leafVisitor = new LeafVisitor();
      List<TerminalNode> leaves = leafVisitor.visit(entry.getValue());
      leaves.remove(0);

      long numberOfVariables = 0;
      long numberOfConstructors = 0;
      long numberOfWildcards = 0;
      long curDepth = 0;
      long depthOfNesting = 0;
      long depthSumOfNesting = 0;

      for (TerminalNode leaf : leaves) {
        String leafText = leaf.getSymbol().getText();
        if (lastTokenWasEquals && !leafText.equals("=")) {
          inGuard = false;
          lastTokenWasEquals = false;
        } else if (lastTokenWasPipe && !leafText.equals("|")) {
          inGuard = true;
          lastTokenWasPipe = false;
        } else if (leafText.equals("|")) {
          lastTokenWasPipe = !lastTokenWasPipe;
        } else if (leafText.equals("=")) {
          lastTokenWasEquals = !lastTokenWasEquals;
        }

        if (!inGuard) {
          continue;
        }

        if (leafText.equals("_")) {
          numberOfWildcards++;
        } else if (Character.isUpperCase(leafText.charAt(0))) {
          numberOfConstructors++;
        } else if (leafText.equals("[") || leafText.equals("(")) {
          depthSumOfNesting++;
          curDepth++;
          if (curDepth > depthOfNesting) {
            depthOfNesting = curDepth;
          }
        } else if (leafText.equals("]") || leafText.equals(")")) {
          curDepth--;
        } else if (Character.isLowerCase(leafText.charAt(0))) {
          numberOfVariables++;
        }
      }

      dataPerPattern.add(
          new PatternMetric(
              entry.getKey(),
              numberOfVariables,
              numberOfConstructors,
              numberOfWildcards,
              depthSumOfNesting,
              depthOfNesting,
              getTreeSize(entry.getValue())));
    }

    for (ParseTree letPattern : HaskellParseTree.getInstance().getLetInList()) {
      LeafVisitor leafVisitor = new LeafVisitor();
      List<TerminalNode> leaves = leafVisitor.visit(letPattern);

      long numberOfVariables = 0;
      long numberOfConstructors = 0;
      long numberOfWildcards = 0;
      long curDepth = 0;
      long depthOfNesting = 0;
      long depthSumOfNesting = 0;

      for (TerminalNode leaf : leaves) {
        String leafText = leaf.getSymbol().getText();
        if (leafText.equals("_")) {
          numberOfWildcards++;
        } else if (Character.isUpperCase(leafText.charAt(0))) {
          numberOfConstructors++;
        } else if (leafText.equals("[") || leafText.equals("(")) {
          depthSumOfNesting++;
          curDepth++;
          if (curDepth > depthOfNesting) {
            depthOfNesting = curDepth;
          }
        } else if (leafText.equals("]") || leafText.equals(")")) {
          curDepth--;
        } else if (Character.isLowerCase(leafText.charAt(0))) {
          numberOfVariables++;
        }
      }

      TerminalNode startToken = (TerminalNode) getLeftMostChild(letPattern);

      dataPerPattern.add(
          new PatternMetric(
              String.format("let (line %d)", startToken.getSymbol().getLine()),
              numberOfVariables,
              numberOfConstructors,
              numberOfWildcards,
              depthSumOfNesting,
              depthOfNesting,
              getTreeSize(letPattern)));
    }

    for (ParseTree letPattern : HaskellParseTree.getInstance().getWhereList()) {
      LeafVisitor leafVisitor = new LeafVisitor();
      List<TerminalNode> leaves = leafVisitor.visit(letPattern);

      long numberOfVariables = 0;
      long numberOfConstructors = 0;
      long numberOfWildcards = 0;
      long curDepth = 0;
      long depthOfNesting = 0;
      long depthSumOfNesting = 0;

      for (TerminalNode leaf : leaves) {
        String leafText = leaf.getSymbol().getText();
        if (leafText.equals("_")) {
          numberOfWildcards++;
        } else if (Character.isUpperCase(leafText.charAt(0))) {
          numberOfConstructors++;
        } else if (leafText.equals("[") || leafText.equals("(")) {
          depthSumOfNesting++;
          curDepth++;
          if (curDepth > depthOfNesting) {
            depthOfNesting = curDepth;
          }
        } else if (leafText.equals("]") || leafText.equals(")")) {
          curDepth--;
        } else if (Character.isLowerCase(leafText.charAt(0))) {
          numberOfVariables++;
        }
      }

      TerminalNode startToken = (TerminalNode) getLeftMostChild(letPattern);

      dataPerPattern.add(
          new PatternMetric(
              String.format("where (line %d)", startToken.getSymbol().getLine()),
              numberOfVariables,
              numberOfConstructors,
              numberOfWildcards,
              depthSumOfNesting,
              depthOfNesting,
              getTreeSize(letPattern)));
    }
  }

  private static void calculateMetrics() {
    for (PatternMetric metric : dataPerPattern) {
      avgNumberOfVariables += metric.numberOfVariables;
      avgNumberOfConstructors += metric.numberOfConstructors;
      avgNumberOfWildcards += metric.numberOfWildcards;
      avgSumDepthOfNesting += metric.depthSumOfNesting;
      avgDepthOfNesting += metric.depthOfNesting;
      avgPatternSize += metric.patternSize;
    }

    avgNumberOfVariables /= dataPerPattern.size();
    avgNumberOfConstructors /= dataPerPattern.size();
    avgNumberOfWildcards /= dataPerPattern.size();
    avgSumDepthOfNesting /= dataPerPattern.size();
    avgDepthOfNesting /= dataPerPattern.size();
    avgPatternSize /= dataPerPattern.size();

    Optional<PatternMetric> maxSumDepth =
        dataPerPattern.stream().max(Comparator.comparingLong(a -> a.depthSumOfNesting));
    if (maxSumDepth.isPresent()) {
      maxSumDepthOfNesting = maxSumDepth.get().depthSumOfNesting;
      maxSumDepthOfNestingName = maxSumDepth.get().name;
    }

    Optional<PatternMetric> maxDepth =
        dataPerPattern.stream().max(Comparator.comparingLong(a -> a.depthOfNesting));
    if (maxDepth.isPresent()) {
      maxDepthOfNesting = maxDepth.get().depthOfNesting;
      maxDepthOfNestingName = maxDepth.get().name;
    }

    Optional<PatternMetric> maxSize =
        dataPerPattern.stream().max(Comparator.comparingLong(a -> a.patternSize));
    if (maxSize.isPresent()) {
      maxPatternSize = maxSize.get().patternSize;
      maxPatternSizeName = maxSize.get().name;
    }
  }

  /**
   * Get the size of the tree in number of nodes.
   *
   * @param tree the tree to be analyzed.
   * @return long representing the number of nodes in the tree.
   */
  private static long getTreeSize(ParseTree tree) {
    long nodes = 1;

    for (int i = 0; i < tree.getChildCount(); i++) {
      nodes += getTreeSize(tree.getChild(i));
    }

    return nodes;
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
}
