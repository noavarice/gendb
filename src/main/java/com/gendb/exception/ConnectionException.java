package com.gendb.exception;

public class ConnectionException extends GenerationException {

  public ConnectionException(
      final String message,
      final Throwable cause,
      final boolean suppressionEnabled,
      final boolean writeStackTrace) {
    super(message, cause, suppressionEnabled, writeStackTrace);
  }
}
