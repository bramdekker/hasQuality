package com.bramdekker.main.util;

import antlr.HaskellParserBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor that collects all leaves of a tree.
 */
public class LeafVisitor extends HaskellParserBaseVisitor<List<TerminalNode>> {
  @Override
  protected List<TerminalNode> defaultResult() {
    return new ArrayList<>();
  }

  @Override
  protected List<TerminalNode> aggregateResult(
      List<TerminalNode> aggregate, List<TerminalNode> nextResult) {
    aggregate.addAll(nextResult);
    return aggregate;
  }

  @Override
  public List<TerminalNode> visitTerminal(TerminalNode node) {
    return List.of(node);
  }
}
