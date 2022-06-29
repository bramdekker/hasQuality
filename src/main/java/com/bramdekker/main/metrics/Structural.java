package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.CallGraph;
import com.bramdekker.main.resources.HaskellParseTree;
import com.bramdekker.main.util.HalsteadVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.*;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;

// Global modularity:
//      - Modules/functions
//      - Modules/type synonyms
//      - Modules/data classes
// Cyclomatic complexity:
//      - If then else, case, patterns, ||, &&
// Number of operators and operands per function

/** Collection of methods that determine structural metrics. */
public class Structural {
  private static final List<StructuralFileMetric> dataPerFile = new ArrayList<>();
  private static final Map<String, CyclomaticComplexityMetric> cycloMap = new HashMap<>();
  private static long totalBranches = 0;
  private static long totalOperators = 0;
  private static long totalOperands = 0;
  private static double avgCyclomaticComplexity = 0;
  private static long maxCyclomaticComplexity = 0;
  private static String maxCyclomaticComplexityName = "";
  private static double avgNumOperators = 0;
  private static long maxNumOperators = 0;
  private static String maxNumOperatorsName = "";
  private static double avgNumOperands = 0;
  private static long maxNumOperands = 0;
  private static String maxNumOperandsName = "";
  private static long totalFunctions = 0;
  private static long totalTypeSynonyms = 0;
  private static long totalDataTypes = 0;
  private static double modulesFunctions = 0;
  private static double avgNumFunctionInModule = 0;
  private static long maxNumFunctionInModule = 0;
  private static String maxNumFunctionInModuleName = "";
  private static long minNumFunctionInModule = 0;
  private static String minNumFunctionInModuleName = "";
  private static double modulesTypeSynonyms = 0;
  private static double avgNumTypeSynonymsInModule = 0;
  private static long maxNumTypeSynonymsInModule = 0;
  private static String maxNumTypeSynonymsInModuleName = "";
  private static long minNumTypeSynonymsInModule = 0;
  private static String minNumTypeSynonymsInModuleName = "";
  private static double modulesDataTypes = 0;
  private static double avgNumDataTypesInModule = 0;
  private static long maxNumDataTypesInModule = 0;
  private static String maxNumDataTypesInModuleName = "";
  private static long minNumDataTypesInModule = 0;
  private static String minNumDataTypesInModuleName = "";

  /**
   * Generate the section for size metrics.
   *
   * @return a report section containing information about size metrics as String.
   */
  public static String getSection() throws IOException {
    collectFileData();

    CallGraph graph = CallGraph.getInstance();

    calculateMetrics();
    StringBuilder structuralSection = new StringBuilder("Structural metrics:\n");

    structuralSection.append(
        getMetricString("Average McCabe's cyclomatic complexity", avgCyclomaticComplexity));
    structuralSection.append(
        getMetricString("Maximum McCabe's cyclomatic complexity", maxCyclomaticComplexity));
    structuralSection.append(
        getMetricString("Maximum McCabe's cyclomatic complexity name", maxCyclomaticComplexityName)
    );
    structuralSection.append(
        getMetricString("Average number of operators per function", avgNumOperators));
    structuralSection.append(getMetricString("Maximum number of operators", maxNumOperators));
    structuralSection.append(
        getMetricString("Maximum number of operators name", maxNumOperatorsName));
    structuralSection.append(
        getMetricString("Average number of operands per function", avgNumOperands));
    structuralSection.append(getMetricString("Maximum number of operands", maxNumOperands));
    structuralSection.append(
        getMetricString("Maximum number of operands name", maxNumOperandsName));

    structuralSection.append(getMetricString("Modules / functions", modulesFunctions));
    structuralSection.append(getMetricString("Modules / type synonyms", modulesTypeSynonyms));
    structuralSection.append(getMetricString("Modules / data types", modulesDataTypes));

    if (dataPerFile.size() > 1) {
      structuralSection.append(
          getMetricString("Average number of functions per module", avgNumFunctionInModule));
      structuralSection.append(
          getMetricString("Maximum number of functions in module", maxNumFunctionInModule));
      structuralSection.append(
          getMetricString(
              "Maximum number of functions in module name", maxNumFunctionInModuleName));
      structuralSection.append(
          getMetricString("Minimum number of functions in module", minNumFunctionInModule));
      structuralSection.append(
          getMetricString(
              "Minimum number of functions in module name", minNumFunctionInModuleName));

      structuralSection.append(
          getMetricString(
              "Average number of type synonyms per module", avgNumTypeSynonymsInModule));
      structuralSection.append(
          getMetricString("Maximum number of type synonyms in module", maxNumTypeSynonymsInModule));
      structuralSection.append(
          getMetricString(
              "Maximum number of type synonyms in module name", maxNumTypeSynonymsInModuleName));
      structuralSection.append(
          getMetricString("Minimum number of type synonyms in module", minNumTypeSynonymsInModule));
      structuralSection.append(
          getMetricString(
              "Minimum number of type synonyms in module name", minNumTypeSynonymsInModuleName));

      structuralSection.append(
          getMetricString("Average number of data types per module", avgNumDataTypesInModule));
      structuralSection.append(
          getMetricString("Maximum number of data types in module", maxNumDataTypesInModule));
      structuralSection.append(
          getMetricString(
              "Maximum number of data types in module name", maxNumDataTypesInModuleName));
      structuralSection.append(
          getMetricString("Minimum number of data types in module", minNumDataTypesInModule));
      structuralSection.append(
          getMetricString(
              "Minimum number of data types in module name", minNumDataTypesInModuleName));
    }

    return structuralSection.toString();
  }

  /** Collect all data per file needed to calculate metrics. */
  private static void collectFileData() throws IOException {
    for (Map.Entry<String, ParseTree> entry :
        HaskellParseTree.getInstance().getTreeDict().entrySet()) {
      HalsteadVisitor visitor = new HalsteadVisitor();
      visitor.visit(entry.getValue());

      cycloMap.putAll(visitor.getFunctionMap());

      dataPerFile.add(
          new StructuralFileMetric(
              entry.getKey(),
              visitor.getFunctions().size(),
              visitor.getTypeSynonyms().size(),
              visitor.getDataTypes().size()));
    }
  }

  /** Sum the data per file to get overall metrics. */
  private static void sumFileData() {
    for (StructuralFileMetric metric : dataPerFile) {
      totalFunctions += metric.functions;
      totalDataTypes += metric.dataTypes;
      totalTypeSynonyms += metric.typeSynonyms;
    }

    for (CyclomaticComplexityMetric metric : cycloMap.values()) {
      totalBranches += metric.getNumBranches();
      totalOperators += metric.getNumOperators();
      totalOperands += metric.getNumOperands();
    }
  }

  /** Calculate all size metrics and store them as static variables. */
  private static void calculateMetrics() {
    sumFileData();

    avgCyclomaticComplexity = (double) totalBranches / totalFunctions + 1;
    avgNumOperators = (double) totalOperators / totalFunctions;
    avgNumOperands = (double) totalOperands / totalFunctions;

    avgNumFunctionInModule = (double) totalFunctions / dataPerFile.size();
    avgNumDataTypesInModule = (double) totalDataTypes / dataPerFile.size();
    avgNumTypeSynonymsInModule = (double) totalTypeSynonyms / dataPerFile.size();
    modulesFunctions = (double) dataPerFile.size() / totalFunctions;
    modulesDataTypes = (double) dataPerFile.size() / totalDataTypes;
    modulesTypeSynonyms = (double) dataPerFile.size() / totalTypeSynonyms;

    Optional<StructuralFileMetric> maxFunctions =
        dataPerFile.stream().max(Comparator.comparingLong(a -> a.functions));
    if (maxFunctions.isPresent() && maxFunctions.get().functions != 0) {
      maxNumFunctionInModule = maxFunctions.get().functions;
      maxNumFunctionInModuleName = maxFunctions.get().name;
    }

    Optional<StructuralFileMetric> minFunctions =
        dataPerFile.stream().min(Comparator.comparingLong(a -> a.functions));
    if (minFunctions.isPresent()) {
      minNumFunctionInModule = minFunctions.get().functions;
      minNumFunctionInModuleName = minFunctions.get().name;
    }

    Optional<StructuralFileMetric> maxTypeSynonyms =
        dataPerFile.stream().max(Comparator.comparingLong(a -> a.typeSynonyms));
    if (maxTypeSynonyms.isPresent() && maxTypeSynonyms.get().typeSynonyms != 0) {
      maxNumTypeSynonymsInModule = maxTypeSynonyms.get().typeSynonyms;
      maxNumTypeSynonymsInModuleName = maxTypeSynonyms.get().name;
    }

    Optional<StructuralFileMetric> minTypeSynonyms =
        dataPerFile.stream().min(Comparator.comparingLong(a -> a.typeSynonyms));
    if (minTypeSynonyms.isPresent()) {
      minNumTypeSynonymsInModule = minTypeSynonyms.get().typeSynonyms;
      minNumTypeSynonymsInModuleName = minTypeSynonyms.get().name;
    }

    Optional<StructuralFileMetric> maxDataTypes =
        dataPerFile.stream().max(Comparator.comparingLong(a -> a.dataTypes));
    if (maxDataTypes.isPresent() && maxDataTypes.get().dataTypes != 0) {
      maxNumDataTypesInModule = maxDataTypes.get().dataTypes;
      maxNumDataTypesInModuleName = maxDataTypes.get().name;
    }

    Optional<StructuralFileMetric> minDataTypes =
        dataPerFile.stream().min(Comparator.comparingLong(a -> a.dataTypes));
    if (minDataTypes.isPresent()) {
      minNumDataTypesInModule = minDataTypes.get().dataTypes;
      minNumDataTypesInModuleName = minDataTypes.get().name;
    }

    Optional<Map.Entry<String, CyclomaticComplexityMetric>> maxCyclo =
        cycloMap.entrySet().stream()
            .max(Comparator.comparingLong(e -> e.getValue().getNumBranches()));
    if (maxCyclo.isPresent()) {
      maxCyclomaticComplexity = maxCyclo.get().getValue().getNumBranches() + 1;
      maxCyclomaticComplexityName = maxCyclo.get().getKey();
    }

    Optional<Map.Entry<String, CyclomaticComplexityMetric>> maxOperators =
        cycloMap.entrySet().stream()
            .max(Comparator.comparingLong(e -> e.getValue().getNumOperators()));
    if (maxOperators.isPresent() && maxOperators.get().getValue().getNumOperators() != 0) {
      maxNumOperators = maxOperators.get().getValue().getNumOperators();
      maxNumOperatorsName = maxOperators.get().getKey();
    }

    Optional<Map.Entry<String, CyclomaticComplexityMetric>> maxOperands =
        cycloMap.entrySet().stream()
            .max(Comparator.comparingLong(e -> e.getValue().getNumOperands()));
    if (maxOperands.isPresent() && maxOperands.get().getValue().getNumOperands() != 0) {
      maxNumOperands = maxOperands.get().getValue().getNumOperands();
      maxNumOperandsName = maxOperands.get().getKey();
    }
  }
}
