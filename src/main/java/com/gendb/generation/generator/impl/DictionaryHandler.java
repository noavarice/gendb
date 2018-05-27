package com.gendb.generation.generator.impl;

import com.gendb.exception.GenerationException;
import com.gendb.generation.GenerationContext;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.Column;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryHandler implements TypeGenerator {

  private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryHandler.class);

  private static final String DATE_FORMAT = "\\d{4}-\\d{2}-\\d{2}";

  private static final String TIME_FORMAT = "\\d{2}:\\d{2}:\\d{2}(:\\d{2})?";

  private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

  private List<Object> values;

  private boolean sequential = false;

  @Override
  public void init(final Column column) throws GenerationException {
    sequential = column.getType().isSequential();
    final Scanner scanner;
    try {
      scanner = new Scanner(new File(column.getType().getDictionary()));
    } catch (FileNotFoundException e) {
      throw new GenerationException("Cannot read dictionary file: " + column.getType().getDictionary(), e, false, true);
    }

    final Map<String, Supplier<Object>> typeToMethod = new HashMap<String, Supplier<Object>>() {{
      put("smallint", scanner::nextShort);
      put("int", scanner::nextInt);
      put("decimal", scanner::nextBigDecimal);
      put("char", scanner::nextLine);
      put("varchar", scanner::nextLine);
      put("timestamp", () -> {
        final String line = scanner.nextLine();
        try {
          return timestampFormat.parse(line);
        } catch (ParseException e) {
          LOGGER.error("Cannot parse '{}' as timestamp", line);
          return null;
        }
      });
      put("date", () -> {
        final String line = scanner.nextLine();
        try {
          return dateFormat.parse(line);
        } catch (ParseException e) {
          LOGGER.error("Cannot parse '{}' as date", line);
          return null;
        }
      });
    }};

    final Supplier<Object> function = typeToMethod.get(column.getType().getName());
    values = new ArrayList<>();
    while (scanner.hasNext()) {
      values.add(function.get());
    }
  }

  @Override
  public Object yield(GenerationContext context) {
    if (sequential) {
      return values.get((int) context.getRowId() - 1);
    }

    return values.get(((int) context.getRandom().getNumber(0, values.size() - 1)));
  }
}
