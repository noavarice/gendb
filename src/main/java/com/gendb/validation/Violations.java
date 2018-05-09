package com.gendb.validation;

/**
 * Contains info messages about various constraint violations
 * at the business logic level
 */
public class Violations {

  public static final String NON_POSITIVE_PRECISION = "Precision is set to non-positive value";

  public static final String NEGATIVE_PRECISION = "Scale is set to negative value";

  public static final String DECIMAL_PROPERTIES_MISSED = "Column has type 'decimal' while precision and scale are not provided";

  public static final String HANDLER_CLASS_NOT_FOUND = "Handler of type '%1$s' not found or cannot be instantiated";

  public static final String NON_UNIQUE_COLUMN_NAMES = "Table '%1$s': columns have non-unique names";

  public static final String NON_UNIQUE_TABLE_NAMES = "Tables have non-unique names";

  public static final String UNKNOWN_FOREIGN_KEY_TARGET_TABLE = "Table '%1$s': unknown table '%2$s' as foreign key reference";

  public static final String NO_CYCLIC_REFERENCES = "Cyclic foreign key referencing found: %1$s";
}
