package com.bramdekker.main.metrics;

/** Encapsulates structural data for a specific file. */
public class StructuralFileMetric {
    public String name;
    public long functions;
    public long typeSynonyms;
    public long dataTypes;

    /**
     * Constructor that initializes all fields accordingly.
     *
     * @param name name of the module.
     * @param functions number of functions in the module.
     * @param typeSynonyms number of type synonyms in the module.
     * @param dataTypes number of data types in the module.
     */
    public StructuralFileMetric(String name, long functions, long typeSynonyms, long dataTypes) {
        this.name = name;
        this.functions = functions;
        this.typeSynonyms = typeSynonyms;
        this.dataTypes = dataTypes;
    }
}
