package com.bramdekker.main.exceptions;

/** Custom exception for invalid commands. */
public class InvalidCommandException extends Exception {
  public InvalidCommandException(String message) {
    super("InvalidCommandException: " + message);
  }
}
