package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;
import static com.bramdekker.main.util.SyllableCounter.numberOfSyllables;

/** Collection of methods that determine readability metrics. */
public class Readability {
  private static final List<ReadabilityFileMetric> dataPerFile = new ArrayList<>();
  private static double commentDensity = 0;

  private static double avgCommentDensity = 0;
  private static double maxCommentDensity = 0;
  private static String maxCommentDensityName = "";

  private static double minCommentDensity = 0;
  private static String minCommentDensityName = "";
  private static long fogIndex = 0;

  private static long avgFogIndex = 0;
  private static long maxFogIndex = 0;
  private static String maxFogIndexName = "";

  /**
   * Generate the section for readability metrics.
   *
   * @return a report section containing information about readability metrics as String.
   */
  public static String getSection() throws IOException {
    collectFileData();
    calculateMetrics();

    StringBuilder readabilitySection = new StringBuilder("Readability metrics:\n");

    readabilitySection.append(getMetricString("Comment density", commentDensity));
    readabilitySection.append(getMetricString("Gunning's fog index", fogIndex));

    if (dataPerFile.size() > 1) {
      readabilitySection.append(getMetricString("Average comment density", avgCommentDensity));
      readabilitySection.append(getMetricString("Maximum comment density", maxCommentDensity));
      readabilitySection.append(
          getMetricString("Maximum comment density file", maxCommentDensityName));
      readabilitySection.append(getMetricString("Minimum comment density", minCommentDensity));
      readabilitySection.append(
          getMetricString("Minimum comment density file", minCommentDensityName));
      readabilitySection.append(getMetricString("Average Gunning's fog index", avgFogIndex));
      readabilitySection.append(getMetricString("Maximum Gunning's fog index", maxFogIndex));
      readabilitySection.append(
          getMetricString("Maximum Gunning's fog index file", maxFogIndexName));
    }

    return readabilitySection.toString();
  }

  /**
   * Calculate all readability metrics and store them as static variables.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void calculateMetrics() throws IOException {
    sumFileData();

    calculateCommentDensity();
  }

  /**
   * Collect all data per file needed to calculate metrics.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void collectFileData() throws IOException {
    for (File file : FileList.getInstance().getHaskellFiles()) {
      Scanner curFileScanner = new Scanner(file);
      String curLine;
      int wordsInFile = 0;
      int sentencesInFile = 0;
      int complexWordsInFile = 0;

      while (curFileScanner.hasNextLine()) {
        curLine = curFileScanner.nextLine();
        if (curLine.startsWith("--")) {
          String[] splittedComment = curLine.substring(2).split(" ");
          for (String part : splittedComment) {
            if (!part.isEmpty()) {
              wordsInFile++;

              if (part.endsWith(".")) {
                sentencesInFile++;
              }
              if (numberOfSyllables(part) >= 3) {
                complexWordsInFile++;
              }
            }
          }
        }
      }

      dataPerFile.add(
          new ReadabilityFileMetric(
              file.getCanonicalPath(), wordsInFile, sentencesInFile, complexWordsInFile));

      curFileScanner.close();
    }
  }

  /** Sum the data per file to get overall metrics. */
  private static void sumFileData() {
    long curMaxFogIndex = 0;
    String curMaxFogIndexName = "";

    long totalWords = 0;
    long totalSentences = 0;
    long totalComplexWords = 0;

    long totalSumFogIndex = 0;

    for (ReadabilityFileMetric metric : dataPerFile) {
      totalWords += metric.words;
      totalSentences += metric.sentences;
      totalComplexWords += metric.complexWords;

      long fogIndex = calculateFogIndex(metric.words, metric.sentences, metric.complexWords);
      totalSumFogIndex += fogIndex;
      if (fogIndex > curMaxFogIndex) {
        curMaxFogIndex = fogIndex;
        curMaxFogIndexName = metric.name;
      }
    }

    fogIndex = calculateFogIndex(totalWords, totalSentences, totalComplexWords);
    avgFogIndex = totalSumFogIndex / dataPerFile.size();
    maxFogIndex = curMaxFogIndex;
    maxFogIndexName = curMaxFogIndexName;
  }

  /**
   * Calculate the fog index.
   *
   * @param words number of words in the text.
   * @param sentences number of sentences in the text.
   * @param complexWords number of complex words in the text.
   * @return the Gunning's fog index.
   */
  private static long calculateFogIndex(long words, long sentences, long complexWords) {
    double wordSentencesRatio = (double) words / sentences;
    double complexWordsPercentage = 100 * (double) complexWords / words;
    return Math.round(0.4 * (wordSentencesRatio + complexWordsPercentage));
  }

  /**
   * Calculate comment density measures.
   *
   * @throws IOException when a file in the FileList resource cannot be found.
   */
  private static void calculateCommentDensity() throws IOException {
    double curMaxCommentDensity = 0;
    String curMaxCommentDensityName = "";
    double curMinCommentDensity = Long.MAX_VALUE;
    String curMinCommentDensityName = "";

    double totalSumCommentDensity = 0;
    long totalNcloc = 0;
    long totalCloc = 0;

    for (SizeFileMetric metric : Size.getDataPerFile()) {
      totalCloc += metric.cloc;
      totalNcloc += metric.ncloc;

      double curCommentDensity = (double) metric.cloc / (metric.ncloc + metric.cloc);
      totalSumCommentDensity += curCommentDensity;

      if (curCommentDensity > curMaxCommentDensity) {
        curMaxCommentDensity = curCommentDensity;
        curMaxCommentDensityName = metric.name;
      }

      if (curCommentDensity < curMinCommentDensity) {
        curMinCommentDensity = curCommentDensity;
        curMinCommentDensityName = metric.name;
      }
    }

    avgCommentDensity = totalSumCommentDensity / dataPerFile.size();
    maxCommentDensity = curMaxCommentDensity;
    maxCommentDensityName = curMaxCommentDensityName;
    minCommentDensity = curMinCommentDensity;
    minCommentDensityName = curMinCommentDensityName;
    commentDensity = (double) totalCloc / (totalNcloc + totalCloc);
  }
}
