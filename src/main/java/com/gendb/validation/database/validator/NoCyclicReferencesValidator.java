package com.gendb.validation.database.validator;

import com.gendb.model.validating.ValidatingForeignKey;
import com.gendb.model.validating.ValidatingTable;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.database.NoCyclicReferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Checks if there is a cycled foreign key chains
 */
public class NoCyclicReferencesValidator implements ConstraintValidator<NoCyclicReferences, List<ValidatingTable>> {

  /**
   * Class for detecting cycles in foreign key declarations
   */
  private class CycleResolver {

    private final Map<String, ValidatingTable> nameToTable;

    private final List<ValidatingTable> tables;

    private CycleResolver(final List<ValidatingTable> tables) {
      this.tables = tables;
      this.nameToTable = tables.stream()
        .collect(Collectors.toMap(ValidatingTable::getName, Function.identity()));
    }

    /**
     * Inner recursive method for cycle detection
     * @see CycleResolver#getCycle()
     */
    private boolean getCycleRecursive(final ValidatingTable table, final List<String> path) {
      if (path.contains(table.getName())) {
        if (path.lastIndexOf(table.getName()) == path.size() - 1) {
          return false;
        }

        path.add(table.getName());
        return true;
      }

      path.add(table.getName());
      final List<ValidatingTable> referencingTables = table.getForeignKeys().stream()
        .map(ValidatingForeignKey::getTargetTable)
        .map(nameToTable::get)
        .collect(Collectors.toList());
      for (final ValidatingTable t: referencingTables) {
        if (getCycleRecursive(t, path)) {
          return true;
        }
      }

      path.remove(path.size() - 1);
      return false;
    }

    private List<String> shorten(final List<String> cycledPath) {
      String cycledTable = null;
      final Set<String> tables = new HashSet<>();
      for (final String table: cycledPath) {
        if (!tables.add(table)) {
          cycledTable = table;
          break;
        }
      }

      final int start = cycledPath.indexOf(cycledTable);
      final int end = cycledPath.lastIndexOf(cycledTable);
      return cycledPath.subList(start, end + 1);
    }

    /**
     * Returns empty list if no cycles detected and non-empty list otherwise
     */
    List<String> getCycle() {
      for (final ValidatingTable t: tables) {
        final List<String> path = new ArrayList<>();
        if (getCycleRecursive(t, path) && path.stream().distinct().count() > 1) {
          return shorten(path);
        }
      }

      return Collections.emptyList();
    }
  }

  @Override
  public boolean isValid(final List<ValidatingTable> tables, final ConstraintValidatorContext context) {
    final List<String> path = new CycleResolver(tables).getCycle();
    if (path.isEmpty()) {
      return true;
    }

    ValidationUtils.addViolation(context, path.stream().collect(Collectors.joining(" -> ")));
    return false;
  }
}
