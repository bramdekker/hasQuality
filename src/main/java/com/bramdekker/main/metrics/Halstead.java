package com.bramdekker.main.metrics;

// - Halstead length: total occurrences of operators + total occurrences of operands
// - Halstead vocabulary: number of unique operators + number of unique operands
// - Halstead volume: N * log2(u)

import com.bramdekker.main.resources.FileList;
import com.bramdekker.main.resources.HaskellParseTree;
import com.bramdekker.main.util.HalsteadVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.bramdekker.main.util.MathUtil.logN;
import static com.bramdekker.main.util.MetricPrinter.getMetricString;

/** Collection of methods that determine halstead metrics. */
public class Halstead {
  private static final List<HalsteadFileMetric> dataPerFile = new ArrayList<>();

  private static long halsteadLength = 0;
  private static long halsteadVocabulary = 0;
  private static double halsteadVolume = 0;
  private static double avgHalsteadLength = 0;
  private static long maxHalsteadLength = 0;
  private static String maxHalsteadLengthName = "";
  private static double avgHalsteadVocabulary = 0;
  private static long maxHalsteadVocabulary = 0;
  private static String maxHalsteadVocabularyName = "";
  private static double avgHalsteadVolume = 0;
  private static double maxHalsteadVolume = 0;
  private static String maxHalsteadVolumeName = "";

  /**
   * Get the section on Halstead metrics.
   *
   * @return a String containing the section with Halstead metrics.
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  public static String getSection() throws IOException {
    collectFileData();
    calculateMetrics();

    StringBuilder halsteadSection = new StringBuilder("Halstead metrics: \n");

    halsteadSection.append(getMetricString("Halstead length", halsteadLength));
    halsteadSection.append(getMetricString("Halstead vocabulary", halsteadVocabulary));
    halsteadSection.append(getMetricString("Halstead volume", halsteadVolume));

    if (dataPerFile.size() > 1) {
      halsteadSection.append(getMetricString("Average Halstead length", avgHalsteadLength));
      halsteadSection.append(getMetricString("Maximum Halstead length", maxHalsteadLength));
      halsteadSection.append(
          getMetricString("Maximum Halstead length file", maxHalsteadLengthName));

      halsteadSection.append(getMetricString("Average Halstead vocabulary", avgHalsteadVocabulary));
      halsteadSection.append(getMetricString("Maximum Halstead vocabulary", maxHalsteadVocabulary));
      halsteadSection.append(
          getMetricString("Maximum Halstead vocabulary file", maxHalsteadVocabularyName));

      halsteadSection.append(getMetricString("Average Halstead volume", avgHalsteadVolume));
      halsteadSection.append(getMetricString("Maximum Halstead volume", maxHalsteadVolume));
      halsteadSection.append(
          getMetricString("Maximum Halstead volume file", maxHalsteadVolumeName));
    }

    return halsteadSection.toString();
  }

  /**
   * Collect all data per file needed to calculate metrics.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void collectFileData() throws IOException {
    for (File file : FileList.getInstance().getHaskellFiles()) {
      ParseTree parseTree =
          HaskellParseTree.getInstance().getTreeDict().get(file.getCanonicalPath());
      HalsteadVisitor halsteadVisitor = new HalsteadVisitor();
      halsteadVisitor.createHalsteadMaps(parseTree);

      long length = halsteadVisitor.getHalsteadLength();
      long vocabulary = halsteadVisitor.getHalsteadVocabulary();

      dataPerFile.add(
          new HalsteadFileMetric(
              file.getCanonicalPath(), length, vocabulary, (length * logN(2, vocabulary))));
    }
  }

  /** Calculate all Halstead metrics and store them as static variables. */
  private static void calculateMetrics() {
    sumFileData();

    avgHalsteadLength = (double) halsteadLength / dataPerFile.size();
    avgHalsteadVocabulary = (double) halsteadVocabulary / dataPerFile.size();
    avgHalsteadVolume = halsteadVolume / dataPerFile.size();

    halsteadVolume = (double) halsteadLength * logN(2, halsteadVocabulary);

    Optional<HalsteadFileMetric> maxLength =
        dataPerFile.stream().max(Comparator.comparingLong(a -> a.halsteadLength));
    if (maxLength.isPresent()) {
      maxHalsteadLength = maxLength.get().halsteadLength;
      maxHalsteadLengthName = maxLength.get().name;
    }

    Optional<HalsteadFileMetric> maxVocabulary =
        dataPerFile.stream().max(Comparator.comparingLong(a -> a.halsteadVocabulary));
    if (maxVocabulary.isPresent()) {
      maxHalsteadVocabulary = maxVocabulary.get().halsteadVocabulary;
      maxHalsteadVocabularyName = maxVocabulary.get().name;
    }

    Optional<HalsteadFileMetric> maxVolume =
        dataPerFile.stream().max(Comparator.comparingDouble(a -> a.halsteadVolume));
    if (maxVolume.isPresent()) {
      maxHalsteadVolume = maxLength.get().halsteadVolume;
      maxHalsteadVolumeName = maxLength.get().name;
    }
  }

  /** Sum the data per file to get overall metrics. */
  private static void sumFileData() {
    for (HalsteadFileMetric metric : dataPerFile) {
      halsteadLength += metric.halsteadLength;
      halsteadVocabulary += metric.halsteadVocabulary;
      halsteadVolume += metric.halsteadVolume;
    }
  }
}
