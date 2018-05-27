package com.gendb.exception;

/**
 * Basic exception for whole application
 */
public class GenerationException extends Exception {

  GenerationException(final String message) {
    super(message);
  }

  public GenerationException(
      final String message,
      final Throwable cause,
      final boolean suppressionEnabled,
      final boolean writeStackTrace) {
    super(message, cause, suppressionEnabled, writeStackTrace);
  }
}
