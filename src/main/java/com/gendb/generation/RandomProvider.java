package com.gendb.generation;

import com.gendb.generation.generator.TypeGenerator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Base class providing random data to {@link TypeGenerator} implementations.
 *
 * Internally utilizes functionality of standard {@link Random} class, but this behaviour can be
 * overridden by providing custom randomness source through appropriate constructor.
 */
public class RandomProvider {

  private static final List<Character> DIGITS = new ArrayList<Character>() {{
    for (char c = '0'; c <= '9'; ++c) {
      add(c);
    }
  }};

  private static final List<Character> LATIN_CHARS = new ArrayList<Character>() {{
    for (char c = 'a'; c <= 'z'; ++c) {
      add(c);
    }

    for (char c = 'A'; c <= 'Z'; ++c) {
      add(c);
    }

    add(' ');
  }};

  private static final int LATIN_CHARS_COUNT = LATIN_CHARS.size();

  private static final long TIMESTAMP_DIFF = Instant.now().toEpochMilli() - Instant.EPOCH.toEpochMilli();

  private final Random rnd;

  public RandomProvider() {
    rnd = new Random();
  }

  public RandomProvider(final Random rnd) {
    this.rnd = rnd;
  }

  private String nextRandomNumeric(final int length) {
    final boolean negative = rnd.nextBoolean();
    final int finalLength = length + (negative ? 1 : 0);
    final byte[] bytes = new byte[finalLength];
    final char[] chars = new char[finalLength];
    rnd.nextBytes(bytes);
    for (int i = 0; i < finalLength; ++i) {
      final int index = (bytes[i] - Byte.MIN_VALUE) % 10;
      chars[i] = DIGITS.get(index);
    }

    if (negative) {
      chars[0] = '-';
    }

    return new String(chars);
  }

  /**
   * Returns floating-point number with double precision
   * between {@code min} and {@code max} inclusively
   */
  public double getNumber(final double min, final double max) {
    if (min == max) {
      return min;
    }

    return min + rnd.nextDouble() * (max - min);
  }

  /**
   * Returns integer between {@code min} and {@code max} inclusively
   */
  public long getNumber(final long min, final long max) {
    if (min == max) {
      return min;
    }

    return min + Math.abs(rnd.nextLong()) % (max - min + 1);
  }

  /**
   * Returns number with fixed decimal point with specified {@code precision}
   * and {@code scale} (which is total number of digits and number of digits
   * after decimal point, accordingly)
   * Resulting number may also be negative
   */
  public BigDecimal getDecimal(final int precision, final int scale) {
    final BigInteger bi = new BigInteger(nextRandomNumeric(precision));
    return new BigDecimal(bi, scale);
  }

  /**
   * Returns string with specified fixed {@code length} and composed of Latin symbols and spaces
   */
  public String getString(final int length) {
    final StringBuilder sb = new StringBuilder(length);
    final byte[] bytes = new byte[length];
    rnd.nextBytes(bytes);
    for (int i = 0; i < length; ++i) {
      final int index = (bytes[i] - Byte.MIN_VALUE) % LATIN_CHARS_COUNT;
      sb.append(LATIN_CHARS.get(index));
    }

    return sb.toString();
  }

  /**
   * Same as {@link RandomProvider#getString(int)}, but length of resulting string
   * may vary from {@code minLength} to {@code maxLength}
   */
  public String getString(final int minLength, final int maxLength) {
    return getString(((int) getNumber(minLength, maxLength)));
  }

  /**
   * Generates random UNIX timestamp from 1970-01-01 00:00:00 to the moment of this JVM instance
   * initialization.
   */
  public Timestamp getTimestamp() {
    final long millis = Instant.EPOCH.toEpochMilli() + Math.abs(rnd.nextLong()) % TIMESTAMP_DIFF;
    return new Timestamp(millis);
  }

  /**
   * Same as {@link RandomProvider#getTimestamp}, but resulting timestamp cannot be earlier that
   * specified {@code min}
   */
  public Timestamp getTimestamp(final long min) {
    final long millis = min + Math.abs(rnd.nextLong()) % (Instant.now().toEpochMilli() - min);
    return new Timestamp(millis);
  }
}
