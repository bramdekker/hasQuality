package com.bramdekker.main.metrics;

/**
 * Encapsulates readability data for a specific file.
 */
public class ReadabilityFileMetric {
  public String name;
  public long words;
  public long sentences;
  public long complexWords;

  /**
   * Constructor that initializes all fields.
   *
   * @param name name of the file.
   * @param words number of words in the comments.
   * @param sentences number of sentences in the comments.
   * @param complexWords number of complex words in the comments.
   */
  public ReadabilityFileMetric(String name, long words, long sentences, long complexWords) {
    this.name = name;
    this.words = words;
    this.sentences = sentences;
    this.complexWords = complexWords;
  }
}
