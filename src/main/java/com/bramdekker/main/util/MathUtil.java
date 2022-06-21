package com.bramdekker.main.util;

import java.util.List;
import java.util.Random;

/**
 * Utility class with mathematical methods.
 */
public class MathUtil {
  /**
   * Calculate logN of x.
   *
   * @param n the base number.
   * @param x the argument for the log.
   * @return the result of logN(x).
   * @throws IllegalArgumentException when base or argument is less or equal than 0.
   */
  public static double logN(int n, long x) {
    if (n < 0 || x < 0) {
      throw new IllegalArgumentException(
          "Both base and argument must be bigger than 0 in logN(x)!");
    }
    return Math.log(x) / Math.log(n);
  }

  /**
   * Generate a random integer of a specific length.
   *
   * @param len the length of the random number.
   * @return the random number.
   * @throws IllegalArgumentException if length is smaller than 2.
   */
  public static int randomNumber(int len) {
    if (len < 2) {
      throw new IllegalArgumentException("Length must be greater or equal to 2!");
    }

    int base = 10 ^ len;
    Random random = new Random();
    return base + random.nextInt(9 * base);
  }

  /**
   * Calculates the Gini-coefficient for the given multiset.
   *
   * @param set a multiset of floating points.
   * @return the Gini-coefficient for the given set.
   * @see <a href="https://en.wikipedia.org/wiki/Gini_coefficient">https://en.wikipedia.org/wiki/Gini_coefficient</a>
   */
  public static double giniCoefficient(List<Double> set) {
    int length = set.size();
    double total = 0;
    double average;
    double differenceSum = 0;

    for (Double valueI : set) {
      total += valueI;

      for (Double valueJ : set) {
        differenceSum += Math.abs(valueI - valueJ);
      }
    }

    average = total / length;

    return differenceSum / (2 * average * Math.pow(length, 2));
  }
}
