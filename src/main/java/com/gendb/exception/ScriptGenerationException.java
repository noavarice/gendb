package com.gendb.exception;

public class ScriptGenerationException extends GenerationException {

  ScriptGenerationException(final String message) {
    super(message);
  }

  public ScriptGenerationException(
      final String message,
      final Throwable cause,
      final boolean suppressionEnabled,
      final boolean writeStackTrace) {
    super(message, cause, suppressionEnabled, writeStackTrace);
  }
}
