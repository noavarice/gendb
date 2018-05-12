package com.gendb.validation;

/**
 * Contains info messages about various constraint violations
 * at the business logic level
 */
public class Violations {

  public static final String NON_POSITIVE_PRECISION = "Precision is set to non-positive value";

  public static final String NEGATIVE_PRECISION = "Scale is set to negative value";

  public static final String DECIMAL_PROPERTIES_MISSED = "ValidatingColumn has type 'decimal' while precision and scale are not provided";

  public static final String HANDLER_CLASS_NOT_FOUND = "Handler of type '%1$s' not found or cannot be instantiated";

  public static final String NON_UNIQUE_COLUMN_NAMES = "ValidatingTable '%1$s': columns have non-unique names";

  public static final String NON_UNIQUE_TABLE_NAMES = "Tables have non-unique names";

  public static final String UNKNOWN_FOREIGN_KEY_TARGET_TABLE = "ValidatingTable '%1$s': unknown table '%2$s' as foreign key reference";

  public static final String NO_CYCLIC_REFERENCES = "Cyclic foreign key referencing found: %1$s";

  public static final String INVALID_NUMERIC_BOUNDARIES = "Minimum boundary (%1$s) more than maximum (%2$s)";

  public static final String ORDERING_COLUMNS_WITH_DIFFERENT_TYPES = "Cannot calculate order between columns "
    + "with different types (table: '%1$s', columns: %2$s)";

  public static final String AMBIGUOUS_COLUMN_ORDER = "Order of columns '%1$s' and '%2$s' is ambiguous";

  public static final String UNKNOWN_COLUMNS_IN_VALUE_ORDER = "Table '%1$s': unknown columns mentioned in 'valueOrder' section";

  public static final String DUPLICATE_COLUMNS_IN_VALUE_ORDER = "Table '%1$s': duplicate columns in single 'valueOrder' section";
}
