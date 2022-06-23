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
  private static double wildcardVariablesRatio = 0;
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
    patternSection.append(getMetricString("Wildcard-variables ratio", wildcardVariablesRatio));
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

  private static void collectFileData() throws IOException {
    for (Map.Entry<String, ParseTree> entry :
        HaskellParseTree.getInstance().getPatternDict().entrySet()) {
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
        if (leafText.equals("|")) {
          break;
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
        } else if (Character.isLowerCase(leafText.charAt(0)) || isLiteral(leafText)) {
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

    for (ParseTree letPattern : HaskellParseTree.getInstance().getLetInPatternsList()) {
      analyzePatternNode(letPattern, "let (line %d)");
    }

    for (ParseTree whereClause : HaskellParseTree.getInstance().getWherePatternsList()) {
      analyzePatternNode(whereClause, "where (line %d)");
    }

    for (ParseTree casePattern : HaskellParseTree.getInstance().getCasePatternsList()) {
      analyzePatternNode(casePattern, "case (line %d)");
    }
  }

  /**
   * Check if the text is a string, character or integer literal.
   *
   * @param text String to be checked.
   * @return true if text is a literal; false otherwise.
   */
  private static boolean isLiteral(String text) {
    return text.startsWith("\"") || text.startsWith("'") || Character.isDigit(text.charAt(0));
  }

  /**
   * Analyze a node representing a pattern.
   *
   * @param node the node.
   * @param format the name format.
   */
  private static void analyzePatternNode(ParseTree node, String format) {
    LeafVisitor leafVisitor = new LeafVisitor();
    List<TerminalNode> leaves = leafVisitor.visit(node);

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
      } else if (Character.isLowerCase(leafText.charAt(0)) || isLiteral(leafText)) {
        numberOfVariables++;
      }
    }

    TerminalNode startToken = (TerminalNode) getLeftMostChild(node);

    dataPerPattern.add(
        new PatternMetric(
            String.format(format, startToken.getSymbol().getLine()),
            numberOfVariables,
            numberOfConstructors,
            numberOfWildcards,
            depthSumOfNesting,
            depthOfNesting,
            getTreeSize(node)));
  }

  /** Calculate all pattern metrics and store them as static variables. */
  private static void calculateMetrics() {
    for (PatternMetric metric : dataPerPattern) {
      avgNumberOfVariables += metric.numberOfVariables;
      avgNumberOfConstructors += metric.numberOfConstructors;
      avgNumberOfWildcards += metric.numberOfWildcards;
      avgSumDepthOfNesting += metric.depthSumOfNesting;
      avgDepthOfNesting += metric.depthOfNesting;
      avgPatternSize += metric.patternSize;
    }

    wildcardVariablesRatio = avgNumberOfWildcards / avgNumberOfVariables;

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
