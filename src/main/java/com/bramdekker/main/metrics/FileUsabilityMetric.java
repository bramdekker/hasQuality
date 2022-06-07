package com.bramdekker.main.metrics;

public class FileUsabilityMetric {
    public String name;
    public long words;
    public long sentences;
    public long complexWords;

    public FileUsabilityMetric(String name, long words, long sentences, long complexWords) {
        this.name = name;
        this.words = words;
        this.sentences = sentences;
        this.complexWords = complexWords;
    }
}
