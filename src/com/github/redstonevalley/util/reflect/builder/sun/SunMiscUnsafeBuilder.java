package com.github.redstonevalley.util.reflect.builder.sun;

import java.lang.reflect.Field;

import com.github.redstonevalley.util.reflect.builder.ReflectiveBuilder;

import sun.misc.Unsafe;

/**
 * A {@link ReflectiveBuilder} that uses {@link Unsafe} to set field values, and thus doesn't need
 * to make the fields accessible. Has a simpler version for Oracle and OpenJDK and a more complex
 * version for Android.
 *
 * @author cryoc
 *
 * @param <T>
 */
@SuppressWarnings("restriction")
public abstract class SunMiscUnsafeBuilder<T> extends ReflectiveBuilder<T> {

  @Override
  protected Object getField(Object sourceObject, Field field) {
    synchronized (sourceObject) {
      Class<?> fieldType = field.getType();
      long fieldOffset = UNSAFE.objectFieldOffset(field);
      if (fieldType.equals(boolean.class)) {
        return getBoolean(sourceObject, fieldOffset);
      } else if (fieldType.equals(byte.class)) {
        return getByte(sourceObject, fieldOffset);
      } else if (fieldType.equals(short.class)) {
        return getShort(sourceObject, fieldOffset);
      } else if (fieldType.equals(char.class)) {
        return getChar(sourceObject, fieldOffset);
      } else if (fieldType.equals(int.class)) {
        return UNSAFE.getInt(sourceObject, fieldOffset);
      } else if (fieldType.equals(long.class)) {
        return UNSAFE.getLong(sourceObject, fieldOffset);
      } else if (fieldType.equals(float.class)) {
        return getFloat(sourceObject, fieldOffset);
      } else if (fieldType.equals(double.class)) {
        return getDouble(sourceObject, fieldOffset);
      } else {
        return UNSAFE.getObject(sourceObject, fieldOffset);
      }
    }
  }

  protected abstract Object getDouble(Object sourceObject, long fieldOffset);

  protected abstract Object getFloat(Object sourceObject, long fieldOffset);

  protected abstract Object getChar(Object sourceObject, long fieldOffset);

  protected abstract Object getShort(Object sourceObject, long fieldOffset);

  protected abstract Object getByte(Object sourceObject, long fieldOffset);

  protected abstract Object getBoolean(Object sourceObject, long fieldOffset);

  protected static final Unsafe UNSAFE;

  static {
    try {
      Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
      unsafeField.setAccessible(true);
      UNSAFE = (Unsafe) unsafeField.get(null);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
        | IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }

  }

  public SunMiscUnsafeBuilder(Class<T> clazz) {
    super(clazz, true);
  }

  @Override
  protected void setFieldInInstance(T objectBeingBuilt, Field field, Object value) {
    Class<?> fieldType = field.getType();
    long fieldOffset = UNSAFE.objectFieldOffset(field);
    if (fieldType.equals(boolean.class)) {
      putBoolean(objectBeingBuilt, fieldOffset, (boolean) value);
    } else if (fieldType.equals(byte.class)) {
      putByte(objectBeingBuilt, fieldOffset, (byte) value);
    } else if (fieldType.equals(short.class)) {
      putShort(objectBeingBuilt, fieldOffset, (short) value);
    } else if (fieldType.equals(char.class)) {
      putChar(objectBeingBuilt, fieldOffset, (char) value);
    } else if (fieldType.equals(int.class)) {
      UNSAFE.putInt(objectBeingBuilt, fieldOffset, (int) value);
    } else if (fieldType.equals(long.class)) {
      UNSAFE.putLong(objectBeingBuilt, fieldOffset, (long) value);
    } else if (fieldType.equals(float.class)) {
      putFloat(objectBeingBuilt, fieldOffset, (float) value);
    } else if (fieldType.equals(double.class)) {
      putDouble(objectBeingBuilt, fieldOffset, (double) value);
    } else {
      UNSAFE.putObject(objectBeingBuilt, fieldOffset, value);
    }
  }

  /** Wraps {@link sun.misc.Unsafe#putDouble} if it's present on this JVM; reimplements it otherwise. */
  protected abstract void putDouble(T objectBeingBuilt, long fieldOffset, double value);

  /** Wraps {@link sun.misc.Unsafe#putFloat} if it's present on this JVM; reimplements it otherwise. */
  protected abstract void putFloat(T objectBeingBuilt, long fieldOffset, float value);

  /** Wraps {@link sun.misc.Unsafe#putChar} if it's present on this JVM; reimplements it otherwise. */
  protected abstract void putChar(T objectBeingBuilt, long fieldOffset, char value);

  /** Wraps {@link sun.misc.Unsafe#putShort} if it's present on this JVM; reimplements it otherwise. */
  protected abstract void putShort(T objectBeingBuilt, long fieldOffset, short value);

  /** Wraps {@link sun.misc.Unsafe#putByte} if it's present on this JVM; reimplements it otherwise. */
  protected abstract void putByte(T objectBeingBuilt, long fieldOffset, byte value);

  /** Wraps {@link sun.misc.Unsafe#putBoolean} if it's present on this JVM; reimplements it otherwise. */
  protected abstract void putBoolean(T objectBeingBuilt, long fieldOffset, boolean value);

  @SuppressWarnings("unchecked")
  @Override
  public T allocateInstance() throws InstantiationException {
    return (T) UNSAFE.allocateInstance(clazz);
  }

}
