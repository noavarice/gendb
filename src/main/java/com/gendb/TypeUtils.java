package com.gendb;

import java.util.HashSet;
import java.util.Set;

public class TypeUtils {

  private static final Set<String> INTEGER_TYPES = new HashSet<String>() {{
    add("smallint");
    add("int");
    add("bigint");
  }};

  private static final Set<String> FLOAT_TYPES = new HashSet<String>() {{
    add("float");
    add("double");
  }};

  private static final Set<String> NUMERIC_TYPES = new HashSet<String>() {{
    addAll(INTEGER_TYPES);
    addAll(FLOAT_TYPES);
    add("decimal");
  }};

  public static boolean isNumeric(final String typeName) {
    return NUMERIC_TYPES.contains(typeName);
  }
}
