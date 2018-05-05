package com.gendb.validation;

/**
 * Contains info messages about various constraint violations
 * at the business logic level
 */
public class Violations {

  public static final String ID_COLUMN_NAME_ALREADY_USED = "%1$s: name of ID column already used";

  public static final String NON_POSITIVE_PRECISION = "Precision is set to non-positive value";

  public static final String NEGATIVE_PRECISION = "Scale is set to negative value";

  public static final String DECIMAL_PROPERTIES_MISSED = "Column has type 'decimal' while precision and scale are not provided";

  public static final String HANDLER_CLASS_NOT_FOUND = "Handler of type '%1$s' not found or cannot be instantiated";
}
