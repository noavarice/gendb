package com.gendb.generation;

import com.gendb.generation.generator.TypeGenerator;
import com.gendb.generation.generator.impl.DateGenerator;
import com.gendb.generation.generator.impl.DecimalGenerator;
import com.gendb.generation.generator.impl.IntegerGenerator;
import com.gendb.generation.generator.impl.SmallintGenerator;
import com.gendb.generation.generator.impl.StringGenerator;
import com.gendb.generation.generator.impl.TimestampGenerator;
import com.gendb.model.pure.Column;
import com.gendb.model.pure.DataType;
import com.gendb.model.pure.Table;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.model.wrapper.extension.common.StringDateWrapper;
import com.gendb.model.wrapper.extension.mysql.MysqlTimestampWrapper;
import com.gendb.model.wrapper.extension.postgres.PostgresTimestampWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InternalGenerator {

  private static final Map<String, Map<String, Class<? extends ValueWrapper>>> TYPE_WRAPPERS =
    new HashMap<String, Map<String, Class<? extends ValueWrapper>>>()
  {{
    put("mysql", new HashMap<String, Class<? extends ValueWrapper>>() {{
      put("smallint", ValueWrapper.class);
      put("int", ValueWrapper.class);
      put("decimal", ValueWrapper.class);
      put("char", StringDateWrapper.class);
      put("varchar", StringDateWrapper.class);
      put("timestamp", MysqlTimestampWrapper.class);
      put("date", StringDateWrapper.class);
    }});

    put("postgres", new HashMap<String, Class<? extends ValueWrapper>>() {{
      put("smallint", ValueWrapper.class);
      put("int", ValueWrapper.class);
      put("decimal", ValueWrapper.class);
      put("char", StringDateWrapper.class);
      put("varchar", StringDateWrapper.class);
      put("timestamp", PostgresTimestampWrapper.class);
      put("date", StringDateWrapper.class);
    }});
  }};

  private static final Map<String, Class<? extends TypeGenerator>> DEFAULT_GENERATORS =
    new HashMap<String, Class<? extends TypeGenerator>>()
  {{
    put("smallint", SmallintGenerator.class);
    put("int", IntegerGenerator.class);
    put("decimal", DecimalGenerator.class);
    put("char", StringGenerator.class);
    put("varchar", StringGenerator.class);
    put("timestamp", TimestampGenerator.class);
    put("date", DateGenerator.class);
  }};

  private static TypeGenerator getGenerator(final DataType type, final RandomValueProvider rnd) {
    final String className = type.getHandlerClass();
    try {
      final TypeGenerator gen;
      if (className == null || className.isEmpty()) {
        gen = DEFAULT_GENERATORS.get(type.getName()).newInstance();
      } else {
        gen = ((Class<? extends TypeGenerator>) Class.forName(className)).newInstance();
      }

      gen.init(type, rnd);
      return gen;
    } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
      return null;
    }
  }

  private final GenerationContext context;

  private final List<ValueWrapper> wrappers;

  private final List<TypeGenerator> generators;

  private final List<Integer> order;

  private long rowId;

  public InternalGenerator(final String dbms, final Table table, final RandomValueProvider rnd) {
    order = table.getColumnGenerationOrder();
    final List<Column> columns = table.getColumns();
    final List<DataType> types = columns.stream().map(Column::getType).collect(Collectors.toList());
    wrappers = types.stream()
      .map(DataType::getName)
      .map(TYPE_WRAPPERS.get(dbms)::get)
      .map(c -> {
        try {
          return c.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
          e.printStackTrace();
        }

        return null;
      })
      .collect(Collectors.toList());
    context = new GenerationContext(table, wrappers);
    generators = types.stream()
      .map(t -> getGenerator(t, rnd))
      .collect(Collectors.toList());
    rowId = 1;
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
