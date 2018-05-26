package com.gendb.exception;

public class ConfigReadingException extends GenerationException {

  public ConfigReadingException(
      final String message,
      final Throwable cause,
      final boolean suppressionEnabled,
      final boolean writeStackTrace) {
    super(message, cause, suppressionEnabled, writeStackTrace);
  }
}
