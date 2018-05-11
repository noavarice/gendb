package com.gendb;

import com.gendb.model.pure.DataType;
import com.gendb.model.wrapper.ValueWrapper;
import com.gendb.model.wrapper.extension.common.StringDateWrapper;
import com.gendb.model.wrapper.extension.mysql.MysqlTimestampWrapper;
import com.gendb.model.wrapper.extension.postgres.PostgresTimestampWrapper;
import com.gendb.random.RandomValueProvider;
import com.gendb.random.generator.TypeGenerator;
import com.gendb.random.generator.impl.DateGenerator;
import com.gendb.random.generator.impl.DecimalGenerator;
import com.gendb.random.generator.impl.IntegerGenerator;
import com.gendb.random.generator.impl.SmallintGenerator;
import com.gendb.random.generator.impl.StringGenerator;
import com.gendb.random.generator.impl.TimestampGenerator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class InternalGenerator {

  private static final Map<String, Map<String, ValueWrapper>> TYPE_WRAPPERS =
    new HashMap<String, Map<String, ValueWrapper>>()
  {{
    put("mysql", new HashMap<String, ValueWrapper>() {{
      put("smallint", new ValueWrapper());
      put("int", new ValueWrapper());
      put("decimal", new ValueWrapper());
      put("char", new StringDateWrapper());
      put("varchar", new StringDateWrapper());
      put("timestamp", new MysqlTimestampWrapper());
      put("date", new StringDateWrapper());
    }});

    put("postgres", new HashMap<String, ValueWrapper>() {{
      put("smallint", new ValueWrapper());
      put("int", new ValueWrapper());
      put("decimal", new ValueWrapper());
      put("char", new StringDateWrapper());
      put("varchar", new StringDateWrapper());
      put("timestamp", new PostgresTimestampWrapper());
      put("date", new StringDateWrapper());
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

  private final List<ValueWrapper> wrappers;

  private final List<TypeGenerator> generators;

  InternalGenerator(final String dbms, final List<DataType> types, final RandomValueProvider rnd) {
    wrappers = types.stream()
      .map(DataType::getName)
      .map(TYPE_WRAPPERS.get(dbms)::get)
      .collect(Collectors.toList());
    generators = types.stream()
      .map(t -> getGenerator(t, rnd))
      .collect(Collectors.toList());
  }

  List<ValueWrapper> getRow() {
    final Iterator<ValueWrapper> wrapperIterator = wrappers.iterator();
    final Iterator<TypeGenerator> generatorIterator = generators.iterator();
    while (wrapperIterator.hasNext()) {
      wrapperIterator.next().setObject(generatorIterator.next().yield());
    }

    return wrappers;
  }
}
