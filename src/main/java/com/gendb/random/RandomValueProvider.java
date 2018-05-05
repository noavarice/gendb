package com.gendb.random;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class RandomValueProvider {

  private static final byte START_CODE = (byte)('0');

  private final Random rnd;

  public RandomValueProvider() {
    rnd = new Random(System.currentTimeMillis());
  }

  public RandomValueProvider(final Random rnd) {
    this.rnd = rnd;
  }

  private Long nextRandom() {
    return rnd.nextLong();
  }

  private Long nextRandom(final long min, final long max) {
    final long diff = max - min;
    return min + rnd.nextLong() % diff;
  }

  private String nextRandomNumeric(final int length) {
    final byte distance = 10;
    final boolean negative = rnd.nextBoolean();
    final int finalLength = length + (negative ? 1 : 0);
    final byte[] bytes = new byte[finalLength];
    rnd.nextBytes(bytes);
    for (int i = 0; i < finalLength; ++i) {
      bytes[i] = (byte)(START_CODE + bytes[i] % distance);
    }

    if (negative) {
      bytes[0] = '-';
    }

    return new String(bytes);
  }

  public Short getSmallint() {
    return nextRandom().shortValue();
  }

  public Short getSmallint(final short min, final short max) {
    return nextRandom(min, max).shortValue();
  }

  public Integer getInt() {
    return nextRandom().intValue();
  }

  public Integer getInt(final int min, final int max) {
    return nextRandom(min, max).intValue();
  }

  public BigDecimal getDecimal(final int precision, final int scale) {
    final BigInteger bi = new BigInteger(nextRandomNumeric(precision));
    return new BigDecimal(bi, scale);
  }

  public String getString(final int length) {
    if (length == 1) {
      return new String(Character.toChars(rnd.nextInt()));
    }

    final StringBuilder sb = new StringBuilder(length * 2);
    for (int i = 0; i < length; ++i) {
      sb.append(Character.toChars(rnd.nextInt()));
    }

    return sb.toString();
  }
}
