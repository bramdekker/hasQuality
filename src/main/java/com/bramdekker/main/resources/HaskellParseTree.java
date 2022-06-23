package com.bramdekker.main.resources;

import antlr.HaskellLexer;
import antlr.HaskellParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.xpath.XPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * A singleton class that contains functionality to generate, get and set a parse tree representing
 * the Haskell program with the help of Antlr.
 */
public class HaskellParseTree {
  private static HaskellParseTree instance;
  private Map<String, ParseTree> treeDict;
  private Map<String, ParseTree> patternDict;
  private List<ParseTree> letInPatternsList;
  private List<ParseTree> wherePatternsList;
  private List<ParseTree> casePatternsList;

  /** Private constructor to make it singleton. */
  private HaskellParseTree() {}

  /**
   * Get the instance of HaskellParseTree. If it does not exist yet, generate one.
   *
   * @return instance of HaskellParseTree with the tree populated.
   * @throws FileNotFoundException if there is a File in the FileList instance that does not exist.
   */
  public static HaskellParseTree getInstance() throws IOException {
    if (instance == null) {
      instance = generateInstance();
    }

    return instance;
  }

  /**
   * Get the contents of a File as a StringBuilder.
   *
   * @param file a File object from which the contents are read.
   * @return StringBuilder that contains the contents of the file.
   * @throws FileNotFoundException if there is a File in the FileList instance that does not exist.
   */
  private String getFileContents(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    StringBuilder contents = new StringBuilder();

    while (scanner.hasNextLine()) {
      contents.append(scanner.nextLine()).append('\n');
    }

    scanner.close();

    return contents.toString();
  }

  /**
   * Generate a new instance of HaskellParseTree. It will create an antlr ParseTree based on the
   * lexer and parser generated by antlr.
   *
   * @return a new instance of HaskellParseTree.
   * @throws FileNotFoundException if there is a File in the FileList instance that does not exist.
   */
  private static HaskellParseTree generateInstance() throws IOException {
    HaskellParseTree parseTree = new HaskellParseTree();
    parseTree.treeDict = new HashMap<>();
    parseTree.patternDict = new HashMap<>();
    parseTree.letInPatternsList = new ArrayList<>();
    parseTree.wherePatternsList = new ArrayList<>();
    parseTree.casePatternsList = new ArrayList<>();

    for (File file : FileList.getInstance().getHaskellFiles()) {
      HaskellLexer lexer =
          new HaskellLexer(CharStreams.fromString(parseTree.getFileContents(file)));
      HaskellParser parser = new HaskellParser(new CommonTokenStream(lexer));

      ParseTree tree = parser.module();
      parseTree.treeDict.put(file.getCanonicalPath(), tree);

      for (ParseTree t : XPath.findAll(tree, "//topdecls/topdecl/decl_no_th/infixexp", parser)) {
        String moduleName = getModuleName(t);
        TerminalNode function = (TerminalNode) getLeftMostChild(t);
        String patternName =
            String.format(
                "%s%s (line %d)", moduleName, function.getText(), function.getSymbol().getLine());
        parseTree.patternDict.put(patternName, t);
      }

      parseTree.letInPatternsList.addAll(
          XPath.findAll(tree, "//aexp/decllist/decls/decl/decl_no_th/infixexp", parser));
      parseTree.wherePatternsList.addAll(
          XPath.findAll(
              tree, "//wherebinds/binds/decllist/decls/decl/decl_no_th/infixexp", parser));
      parseTree.casePatternsList.addAll(XPath.findAll(tree, "//alts/alt/pat", parser));

      //                  JFrame frame = new JFrame("Antlr parse tree");
      //                  JPanel panel = new JPanel();
      //                  TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()),
      // tree);
      //                  viewer.setScale(1.0); // Scale a little
      //                  panel.add(viewer);
      //                  frame.add(panel);
      //                  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //                  frame.pack();
      //                  frame.setVisible(true);
    }

    return parseTree;
  }

  private static String getModuleName(ParseTree tree) {
    ParseTree moduleContentNode = tree;
    while (moduleContentNode != null) {
      if (moduleContentNode instanceof HaskellParser.Module_contentContext) {
        break;
      }
      moduleContentNode = moduleContentNode.getParent();
    }

    if (moduleContentNode != null) {
      return getLeftMostChild(moduleContentNode.getChild(1)).getText() + ".";
    }

    return "";
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
   * Getter for the treeDict field.
   *
   * @return a dictionary with ParseTree per module.
   */
  public Map<String, ParseTree> getTreeDict() {
    return this.treeDict;
  }

  /**
   * Getter for the patternTree field.
   *
   * @return a dictionary with all patterns ParseTree in all modules.
   */
  public Map<String, ParseTree> getPatternDict() {
    return this.patternDict;
  }

  /**
   * Getter for the letInList field.
   *
   * @return a List with all let-in expressions in all modules.
   */
  public List<ParseTree> getLetInPatternsList() {
    return this.letInPatternsList;
  }

  /**
   * Getter for the whereList field.
   *
   * @return a List with all where clauses in all modules.
   */
  public List<ParseTree> getWherePatternsList() {
    return this.wherePatternsList;
  }

  /**
   * Getter for the caseList field.
   *
   * @return a List with all case patterns in all modules.
   */
  public List<ParseTree> getCasePatternsList() {
    return this.casePatternsList;
  }
}
