package com.gendb.validation;

/**
 * Contains info messages about various constraint violations
 * at the business logic level
 */
public class Violations {

  public static final String UNKNOWN_ID_COLUMN = "%1$s: unknown columns are used as part of table ID";

  public static final String NON_POSITIVE_PRECISION = "Precision is set to non-positive value";

  public static final String NEGATIVE_PRECISION = "Scale is set to negative value";

  public static final String DECIMAL_PROPERTIES_MISSED = "Column has type 'decimal' while precision and scale are not provided";
}
