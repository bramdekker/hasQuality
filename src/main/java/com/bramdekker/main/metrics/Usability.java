package com.bramdekker.main.metrics;

import com.bramdekker.main.resources.FileList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;

public class Usability {
    private static final List<FileUsabilityMetric> dataPerFile = new ArrayList<>();
    private static final long cyclomaticComplexity = 1;
    private static long commentDensity = 0;
    private static long readability = 0;
    private static long fogIndex = 0;

    public static String getSection() throws IOException {
        collectFileData();
        calculateMetrics();

        StringBuilder usabilitySection = new StringBuilder("Usability metrics:\n");

        usabilitySection.append(getMetricString("Comment density", commentDensity));
        usabilitySection.append(getMetricString("Readability", commentDensity));
        usabilitySection.append(getMetricString("Gunning's fog index", fogIndex));

        return usabilitySection.toString();
    }

    // TODO: replace cyclomaticComplexity with the real value.
    private static void calculateMetrics() {
        commentDensity = Size.getCLOC() / Size.getLOC();
        readability = 0.295 * getAvgVariableLength() - 0.499 * Size.getNCLOC()
                + 0.13 * cyclomaticComplexity;
    }

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
                    String[] splittedComment = curLine.split(" ");
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
                    new FileUsabilityMetric(
                            file.getCanonicalPath(),
                            wordsInFile,
                            sentencesInFile,
                            complexWordsInFile));

            curFileScanner.close();
        }
}
