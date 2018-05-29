package com.gendb.generation.generator.impl;

import com.gendb.generation.GenerationContext;
import com.gendb.generation.RandomProvider;
import com.gendb.generation.generator.TypeGenerator;
import com.gendb.model.pure.Column;
import com.gendb.model.pure.DataType;
import com.gendb.util.Types;
import java.util.function.Function;

/**
 * Performs value generation for columns with data types {@link Types#CHAR}
 * and {@link Types#VARCHAR}
 */
public class StringGenerator implements TypeGenerator {

  private Function<RandomProvider, String> f;

  @Override
  public void init(final Column column) {
    final DataType t = column.getType();
    if (t.getLength() != null) {
      final int fixedLength = t.getLength();
      f = rnd -> rnd.getString(fixedLength);
      return;
    }

    final int minLength = t.getMinLength();
    final int maxLength = t.getMaxLength();
    f = minLength == maxLength ? (rnd -> rnd.getString(minLength))
                               : (rnd -> rnd.getString(minLength, maxLength));
  }

  @Override
  public Object yield(final GenerationContext context) {
    return f.apply(context.getRandom());
  }
}
