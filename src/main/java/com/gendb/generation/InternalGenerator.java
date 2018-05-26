package com.gendb.generation;

import com.gendb.exception.IncorrectTypeException;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.generation.generator.impl.DecimalGenerator;
import com.gendb.generation.generator.impl.IntegerGenerator;
import com.gendb.generation.generator.impl.StringGenerator;
import com.gendb.generation.generator.impl.TimestampGenerator;
import com.gendb.model.pure.Column;
import com.gendb.model.pure.DataType;
import com.gendb.model.pure.Table;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.model.wrapper.extension.common.DateWrapper;
import com.gendb.model.wrapper.extension.common.StringWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class InternalGenerator {

  private static final Map<String, Class<? extends ValueWrapper>> TYPE_WRAPPERS =
    new HashMap<String, Class<? extends ValueWrapper>>()
  {{
    put("smallint", ValueWrapper.class);
    put("int", ValueWrapper.class);
    put("decimal", ValueWrapper.class);
    put("char", StringWrapper.class);
    put("varchar", StringWrapper.class);
    put("timestamp", StringWrapper.class);
    put("date", DateWrapper.class);
  }};

  private static final Map<String, Class<? extends TypeGenerator>> DEFAULT_GENERATORS =
    new HashMap<String, Class<? extends TypeGenerator>>()
  {{
    put("smallint", IntegerGenerator.class);
    put("int", IntegerGenerator.class);
    put("decimal", DecimalGenerator.class);
    put("char", StringGenerator.class);
    put("varchar", StringGenerator.class);
    put("timestamp", TimestampGenerator.class);
    put("date", TimestampGenerator.class);
  }};

  private static TypeGenerator getGenerator(final Column column) throws IncorrectTypeException {
    final DataType type = column.getType();
    final String className = type.getHandlerClass();
    final TypeGenerator gen;
    try {
      if (className == null || className.isEmpty()) {
        gen = DEFAULT_GENERATORS.get(type.getName()).newInstance();
      } else {
        gen = ((Class<? extends TypeGenerator>) Class.forName(className)).newInstance();
      }
    } catch (ReflectiveOperationException e) {
      throw new IncorrectTypeException("Failed to instantiate type generator class", e, false, true);
    }

    gen.init(column);
    return gen;
  }

  private final GenerationContext context;

  private final List<ValueWrapper> wrappers;

  private final List<TypeGenerator> generators;

  private final List<Integer> order;

  private long rowId;

  private InternalGenerator(
      final List<TypeGenerator> generators,
      final List<ValueWrapper> wrappers,
      final List<Integer> generationOrder,
      final GenerationContext context) {
    this.generators = generators;
    this.wrappers = wrappers;
    this.order = generationOrder;
    this.context = context;
    rowId = 1;
  }

  public static InternalGenerator createGenerator(
      final Table table,
      final RandomProvider rnd) throws IncorrectTypeException {
    final List<Integer> order = table.getColumnGenerationOrder();
    final List<Column> columns = table.getColumns();
    final List<Class<? extends ValueWrapper>> classes = columns.stream()
        .map(Column::getType)
        .map(DataType::getName)
        .map(TYPE_WRAPPERS::get)
        .collect(Collectors.toList());
    final List<TypeGenerator> generators = new ArrayList<>(columns.size());
    final List<ValueWrapper> wrappers = new ArrayList<>(columns.size());
    for (int i = 0; i < columns.size(); ++i) {
      generators.add(getGenerator(columns.get(i)));
      try {
        wrappers.add(classes.get(i).newInstance());
      } catch (ReflectiveOperationException e) {
        throw new IncorrectTypeException("Failed to instantiate value wrapper class", e, false, true);
      }
    }

    final GenerationContext context = new GenerationContext(table, wrappers, rnd);
    return new InternalGenerator(generators, wrappers, order, context);
  }

  public List<ValueWrapper> getRow() {
    context.setRowId(rowId);
    for (final int i: order) {
      final Object result = generators.get(i).yield(context);
      wrappers.get(i).setObject(result);
    }

    ++rowId;
    return wrappers;
  }
}
