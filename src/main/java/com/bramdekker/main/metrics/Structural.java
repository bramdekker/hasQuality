package com.bramdekker.main.metrics;

import java.io.IOException;
import java.util.List;

import static com.bramdekker.main.util.MetricPrinter.getMetricString;

// Global modularity:
//      - Modules/functions
//		- Modules/type synonyms
//		- Modules/data classes

/** Collection of methods that determine structural metrics. */
public class Structural {
    private static List<StructuralFileMetric> dataPerFile;
    private static long modulesFunctions = 0;
    private static long modulesTypeSynonyms = 0;
    private static long modulesDataTypes = 0;

    /**
     * Generate the section for size metrics.
     *
     * @return a report section containing information about size metrics as String.
     */
    public static String getSection() throws IOException {
        collectFileData();
//
//        calculateMetrics();
        StringBuilder structuralSection = new StringBuilder("Structural metrics:\n");

        structuralSection.append(getMetricString("Modules / functions", modulesFunctions));
        structuralSection.append(getMetricString("Modules / type synonyms", modulesTypeSynonyms));
        structuralSection.append(getMetricString("Modules / data types", modulesDataTypes));

//        if (dataPerFile.size() > 1) {
//            sizeSection.append(getMetricString("Average module size (NCLOC)", avgModuleSize));
//            sizeSection.append(getMetricString("Maximum module size (NCLOC)", maxModuleSize));
//            sizeSection.append(getMetricString("Maximum module size file", maxModuleName));
//        }

        return structuralSection.toString();
    }

    /**
     * Collect all data per file needed to calculate metrics.
     */
    private static void collectFileData() {

    }
}
