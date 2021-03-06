package com.gendb.exception;

public class IncorrectTypeException extends GenerationException {

  public IncorrectTypeException(final String message) {
    super(message);
  }

  public IncorrectTypeException(
      final String message,
      final Throwable cause,
      final boolean suppressionEnabled,
      final boolean writeStackTrace) {
    super(message, cause, suppressionEnabled, writeStackTrace);
  }
}
