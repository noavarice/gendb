package com.gendb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Generator {

  public static void fromStream(final InputStream input, final OutputStream output)
      throws IOException {
    System.out.println(GeneratorUtils.isValidStructure(input));
  }
}
