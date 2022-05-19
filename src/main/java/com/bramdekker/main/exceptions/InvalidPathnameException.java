package com.bramdekker.main.exceptions;

/** Custom exception for invalid (non-existing) pathnames. */
public class InvalidPathnameException extends Exception {
  public InvalidPathnameException(String message) {
    super("InvalidPathnameException: " + message);
  }
}
