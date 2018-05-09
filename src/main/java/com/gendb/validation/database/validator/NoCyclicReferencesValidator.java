package com.gendb.validation.database.validator;

import com.gendb.model.ForeignKey;
import com.gendb.model.Table;
import com.gendb.validation.ValidationUtils;
import com.gendb.validation.database.NoCyclicReferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
public class NoCyclicReferencesValidator implements ConstraintValidator<NoCyclicReferences, List<Table>> {

  /**
   * Class for detecting cycles in foreign key declarations
   */
  private class CycleResolver {

    private final Map<String, Table> nameToTable;

    private final List<Table> tables;

    private CycleResolver(final List<Table> tables) {
      this.tables = tables;
      this.nameToTable = tables.stream()
        .collect(Collectors.toMap(Table::getName, Function.identity()));
    }

    /**
     * Inner recursive method for cycle detection
     * @see CycleResolver#getCycle()
     */
    private boolean getCycleRecursive(final Table table, final List<String> path) {
      if (path.contains(table.getName())) {
        path.add(table.getName());
        return true;
      }

      path.add(table.getName());
      final List<Table> referencingTables = table.getForeignKeys().stream()
        .map(ForeignKey::getTargetTable)
        .map(nameToTable::get)
        .collect(Collectors.toList());
      for (final Table t: referencingTables) {
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
      for (final Table t: tables) {
        final List<String> path = new ArrayList<>();
        if (getCycleRecursive(t, path)) {
          return shorten(path);
        }
      }

      return Collections.emptyList();
    }
  }

  @Override
  public boolean isValid(final List<Table> tables, final ConstraintValidatorContext context) {
    final List<String> path = new CycleResolver(tables).getCycle();
    if (path.isEmpty()) {
      return true;
    }

    ValidationUtils.addViolation(context, path.stream().collect(Collectors.joining(" -> ")));
    return false;
  }
}
