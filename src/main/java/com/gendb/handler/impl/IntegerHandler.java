package com.gendb.handler.impl;

import com.gendb.handler.TypeHandler;
import com.gendb.model.DataType;
import com.gendb.model.wrapper.DefaultWrapper;
import com.gendb.random.RandomValueProvider;
import java.util.function.LongSupplier;

public class IntegerHandler implements TypeHandler {

  private static final long MAX_UNSIGNED_INT = (long)(Integer.MAX_VALUE) - Integer.MIN_VALUE;

  private LongSupplier f;

  @Override
  public void init(final DataType type, final RandomValueProvider provider) {
    if (type.isUnsigned()) {
      final boolean invalidMin = type.getMin() == null || type.getMin() < 0;
      final boolean invalidMax = type.getMax() == null || type.getMax() > MAX_UNSIGNED_INT;
      if (invalidMin && invalidMax) {
        f = provider::getUnsignedInt;
        return;
      }


      final long min = invalidMin ? 0 : type.getMin().longValue();
      final long max = invalidMax ? MAX_UNSIGNED_INT : type.getMax().longValue();
      f = () -> provider.getUnsignedInt(min, max);
      return;
    }

    final boolean invalidMin = type.getMin() == null || type.getMin() < Integer.MIN_VALUE;
    final boolean invalidMax = type.getMax() == null || type.getMax() > Integer.MAX_VALUE;
    if (invalidMin && invalidMax) {
      f = provider::getSignedInt;
      return;
    }

    final int min = invalidMin ? Integer.MIN_VALUE : type.getMin().intValue();
    final int max = invalidMax ? Integer.MAX_VALUE : type.getMax().intValue();
    f = () -> provider.getSignedInt(min, max);
  }

  @Override
  public DefaultWrapper yield() {
    return new DefaultWrapper(f.getAsLong());
  }
}
