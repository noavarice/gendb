package com.gendb.util;

import com.gendb.exception.IncorrectTypeException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(TypeUtils.class);

  private static final Set<String> INTEGER_TYPES = new HashSet<String>() {{
    add(Types.SMALLINT.getName());
    add(Types.INT.getName());
  }};

  private static final Set<String> FLOAT_TYPES = new HashSet<String>() {{
    add(Types.FLOAT.getName());
    add(Types.DOUBLE.getName());
  }};

  private static final Set<String> NUMERIC_TYPES = new HashSet<String>() {{
    addAll(INTEGER_TYPES);
    addAll(FLOAT_TYPES);
    add(Types.DECIMAL.getName());
  }};

  private static final Set<String> STRING_TYPES = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
    addAll(Arrays.asList(Types.CHAR.getName(), Types.VARCHAR.getName()));
  }};

  private static final Map<Types, Double> TYPE_TO_MAX_SIGNED = new HashMap<Types, Double>() {{
    put(Types.SMALLINT, (double)Short.MAX_VALUE);
    put(Types.INT, (double)(Integer.MAX_VALUE));
    put(Types.FLOAT, (double)Float.MAX_VALUE);
    put(Types.DOUBLE, Double.MAX_VALUE);
  }};

  private static final Map<Types, Double> TYPE_TO_MIN_SIGNED = new HashMap<Types, Double>() {{
    put(Types.SMALLINT, (double)Short.MIN_VALUE);
    put(Types.INT, (double)Integer.MIN_VALUE);
    put(Types.FLOAT, (double)Float.MIN_VALUE);
    put(Types.DOUBLE, Double.MIN_VALUE);
  }};

  public static boolean isNumeric(final String typeName) {
    return NUMERIC_TYPES.contains(typeName);
  }

  public static boolean isString(final String typeName) {
    return STRING_TYPES.contains(typeName);
  }

  public static double getMinValue(final String name) throws IncorrectTypeException {
    if (!NUMERIC_TYPES.contains(name)) {
      LOGGER.error("Numeric type expected, but '{}' passed", name);
      throw new IncorrectTypeException(name);
    }

    return TYPE_TO_MIN_SIGNED.get(Types.fromName(name));
  }

  public static double getMaxValue(final String name) throws IncorrectTypeException {
    if (!NUMERIC_TYPES.contains(name)) {
      LOGGER.error("Numeric type expected, but '{}' passed", name);
      throw new IncorrectTypeException(name);
    }

    return TYPE_TO_MAX_SIGNED.get(Types.fromName(name));
  }
}
