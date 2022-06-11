package com.bramdekker.main.metrics;

/**
 * Encapsulates data for a specific pattern.
 */
public class PatternMetric {
    public String name;
    public long numberOfVariables;
    public long numberOfConstructors;
    public long numberOfWildcards;
    public long depthSumOfNesting;
    public long depthOfNesting;
    public long patternSize;

    /**
     * Constructor that initializes all fields of the class.
     *
     * @param name name of the function.
     * @param numberOfVariables number of variables in the pattern.
     * @param numberOfConstructors number of constructors in the pattern.
     * @param numberOfWildcards number of wildcards in the pattern.
     * @param depthSumOfNesting sum of the depths of all the nested patterns.
     * @param depthOfNesting depth of nesting in the pattern.
     * @param patternSize size of the pattern as number of nodes.
     */
    public PatternMetric(String name, long numberOfVariables, long numberOfConstructors, long numberOfWildcards, long depthSumOfNesting, long depthOfNesting, long patternSize) {
        this.name = name;
        this.numberOfVariables = numberOfVariables;
        this.numberOfConstructors = numberOfConstructors;
        this.numberOfWildcards = numberOfWildcards;
        this.depthSumOfNesting = depthSumOfNesting;
        this.depthOfNesting = depthOfNesting;
        this.patternSize = patternSize;
    }
}
