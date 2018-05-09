package com.gendb.random.generator.impl;

import com.gendb.model.DataType;
import com.gendb.random.RandomValueProvider;
import com.gendb.random.generator.TypeGenerator;
import java.util.function.LongSupplier;

public class SmallintGenerator implements TypeGenerator {

  private static final int MAX_UNSIGNED_SHORT = (int)(Short.MAX_VALUE) - Short.MIN_VALUE;

  private LongSupplier f;

  @Override
  public void init(DataType type, RandomValueProvider provider) {
    if (type.isUnsigned()) {
      final boolean invalidMin = type.getMin() == null || type.getMin() < 0;
      final boolean invalidMax = type.getMax() == null || type.getMax() > MAX_UNSIGNED_SHORT;
      if (invalidMin && invalidMax) {
        f = provider::getUnsignedSmallint;
        return;
      }


      final int min = invalidMin ? 0 : type.getMin().shortValue();
      final int max = invalidMax ? MAX_UNSIGNED_SHORT : type.getMax().shortValue();
      f = () -> provider.getUnsignedSmallint(min, max);
      return;
    }

    final boolean invalidMin = type.getMin() == null || type.getMin() < Short.MIN_VALUE;
    final boolean invalidMax = type.getMax() == null || type.getMax() > Short.MAX_VALUE;
    if (invalidMin && invalidMax) {
      f = provider::getSignedSmallint;
      return;
    }

    final int min = invalidMin ? Short.MIN_VALUE : type.getMin().intValue();
    final int max = invalidMax ? Short.MAX_VALUE : type.getMax().intValue();
    f = () -> provider.getSignedInt(min, max);
  }

  @Override
  public Object yield() {
    return f.getAsLong();
  }
}
