package com.gendb.validation;

/**
 * Contains info messages about various constraint violations
 * at the business logic level
 */
public class Violations {

  public static final String DECIMAL_PROPERTIES_MISSED = "Column has type 'decimal' while precision and scale are not provided";

  public static final String HANDLER_CLASS_NOT_FOUND = "Handler of type '%1$s' not found or cannot be instantiated";

  public static final String NON_UNIQUE_COLUMN_NAMES = "Table '%1$s': columns have non-unique names";

  public static final String NON_UNIQUE_TABLE_NAMES = "Tables have non-unique names";

  public static final String UNKNOWN_FOREIGN_KEY_TARGET_TABLE = "Table '%1$s': unknown table '%2$s' as foreign key reference";

  public static final String NO_CYCLIC_REFERENCES = "Cyclic foreign key referencing found: %1$s";

  public static final String INVALID_NUMERIC_BOUNDARIES = "Minimum boundary (%1$s) more than maximum (%2$s)";

  public static final String ORDERING_COLUMNS_WITH_DIFFERENT_TYPES = "Cannot calculate order between columns "
    + "with different types (table: '%1$s', columns: %2$s)";

  public static final String AMBIGUOUS_COLUMN_ORDER = "Order of columns '%1$s' and '%2$s' is ambiguous";

  public static final String UNKNOWN_COLUMNS_IN_VALUE_ORDER = "Table '%1$s': unknown columns mentioned in 'valueOrder' section";

  public static final String DUPLICATE_COLUMNS_IN_VALUE_ORDER = "Table '%1$s': duplicate columns in single 'valueOrder' section";

  public static final String UNORDERED_DISTRIBUTION_POINTS = "Points forming distribution are not ordered";

  public static final String INCOMPLETE_DISTRIBUTION_PERCENTAGE = "Total distribution percentage must be equal to 100";

  public static final String DISTRIBUTION_INTERVAL_PERCENTAGE_OUT_OF_BOUNDS = "Distribution interval percentage is out of bounds [0, 100]";

  public static final String DICTIONARY_NOT_EXISTS = "Dictionary file '%1$s' does not exist";

  public static final String PRECISION_NOT_LESS_THAN_SCALE = "Precision (%1$d) is less than decimal scale (%2$d)";

  public static final String STRING_PROPERTIES_MISSED = "For column with type 'CHAR[n]' or"
      + "'VARCHAR[n]' must be either set 'length' or both of 'minLength', 'maxLength' attributes";

  public static final String STRING_MIN_LENGTH_MORE_THAN_MAX_LENGTH = "For column with type 'CHAR[n]'"
      + " or 'VARCHAR[n]' value of 'minLength' attribute must be no more than 'maxLength' value";
}
