package com.gendb.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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

  private static final Set<String> STRING_TYPES = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
    addAll(Arrays.asList("char", "varchar"));
  }};

  public static boolean isNumeric(final String typeName) {
    return NUMERIC_TYPES.contains(typeName);
  }

  public static boolean isString(final String typeName) {
    return STRING_TYPES.contains(typeName);
  }
}
