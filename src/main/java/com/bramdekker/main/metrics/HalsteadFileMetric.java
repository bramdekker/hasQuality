package com.bramdekker.main.metrics;

/**
 * Encapsulates Halstead data for a specific file.
 */
public class HalsteadFileMetric {
  public String name;
  public long halsteadLength;
  public long halsteadVocabulary;
  public long halsteadVolume;

  /**
   * Constructor that sets all data accordingly.
   *
   * @param name the name of the file.
   * @param halLength total occurrences of operators + total occurrences of operands.
   * @param halVocabulary unique occurrences of operators + unique occurrences of operands.
   * @param halVolume the Halstead volume.
   */
  public HalsteadFileMetric(String name, long halLength, long halVocabulary, long halVolume) {
    this.name = name;
    this.halsteadLength = halLength;
    this.halsteadVocabulary = halVocabulary;
    this.halsteadVolume = halVolume;
  }
}
