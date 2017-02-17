package com.github.redstonevalley.util.reflect.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.Nullable;

/**
 * A builder that uses reflection, and therefore can be applied to classes that don't define a
 * builder class of their own.
 *
 * @author cryoc
 *
 * @param <T> The class that this builder builds.
 */
public abstract class ReflectiveBuilder<T> {
  protected final HashMap<Field, Object> fieldValues = new HashMap<>();
  protected final HashSet<Field> explicitlyNullFields = new HashSet<>();
  protected final Class<T> clazz;
  protected final boolean allowsTypePunning;

  /**
   * @return Whether {@code value} can be assigned to a field of type {@code clazz}, taking into
   *         account widening conversions, unboxing of primitive values, and that {@code value} may
   *         be null.
   */
  protected static boolean isReallyAssignableFrom(Class<?> clazz, @Nullable Object value) {
    if (clazz.equals(byte.class)) {
      return value != null && value instanceof Byte;
    } else if (clazz.equals(short.class)) {
      return value != null && (value instanceof Byte || value instanceof Short);
    } else if (clazz.equals(char.class)) {
      return value != null && (value instanceof Byte || value instanceof Character);
    } else if (clazz.equals(int.class)) {
      return value != null
          && (value instanceof Byte
              || value instanceof Short
              || value instanceof Character
              || value instanceof Integer);
    } else if (clazz.equals(long.class)) {
      return value != null
          && (value instanceof Byte
              || value instanceof Short
              || value instanceof Character
              || value instanceof Integer
              || value instanceof Long);
    } else if (clazz.equals(boolean.class)) {
      return value != null && value instanceof Boolean;
    } else if (clazz.equals(float.class)) {
      return value != null && value instanceof Float;
    } else if (clazz.equals(double.class)) {
      return value != null && (value instanceof Float || value instanceof Double);
    } else {
      return value == null || clazz.isInstance(value);
    }
  }

  protected ReflectiveBuilder(Class<T> clazz, boolean allowsTypePunning) {
    this.clazz = clazz;
    this.allowsTypePunning = allowsTypePunning;
  }

  /**
   * Adds a field to the list of fields to be set reflectively by {@link #build()}.
   *
   * @param field
   *          The field to set.
   * @param value
   *          The value to set the field to. If null, the field will be set
   *          to null after any constructor has returned.
   *
   * @throws ClassCastException
   *           if {@code field} isn't a field of T or a superclass of T and
   *           this builder doesn't allow type punning, or if {@code value}
   *           isn't null or an instance of {@code field}'s type.
   */
  public synchronized void setField(Field field, Object value) {
    if (!allowsTypePunning && !(field.getDeclaringClass().isAssignableFrom(clazz))) {
      throw new ClassCastException(String.format("Class %s doesn't have field %s", clazz, field));
    }
    if (value == null) {
      explicitlyNullFields.add(field);
    } else {
      Class<?> fieldType = field.getType();
      if (!isReallyAssignableFrom(fieldType, value)) {
        throw new ClassCastException(
            String.format(
                "Value %s can't be cast to %s to assign to field %s",
                value,
                fieldType,
                field));
      }
      explicitlyNullFields.remove(field);
    }
    fieldValues.put(field, value);
  }

  /**
   * Removes a field from the list of fields to be set reflectively by {@link #build()}.
   *
   * @return true if the field was removed; false if it was already absent.
   */
  public synchronized boolean unsetField(Field field) {
    return (fieldValues.remove(field) != null) || explicitlyNullFields.remove(field);
  }

  protected abstract T allocateInstance() throws InstantiationException;

  protected abstract void setFieldInInstance(T objectBeingBuilt, Field field, Object value);

  protected abstract Object getField(Object sourceObject, Field field);

  /**
   * Shallowly copies all the given object's fields into this builder. Those fields must all exist
   * in T.
   *
   * @param source The object to copy.
   * @throws ClassCastException If {@code source} contains a field that is not in T and this builder
   *           does not allow type punning.
   */
  public void clone(Object source) {
    Class<?> sourceClass = source.getClass();
    while (!(sourceClass.equals(Object.class))) {
      for (Field field : sourceClass.getDeclaredFields()) {
        if (!(Modifier.isStatic(field.getModifiers()))) {
          setField(field, getField(source, field));
        }
      }
      sourceClass = sourceClass.getSuperclass();
    }
  }

  /**
   * Shallowly copies all the fields into this builder that are in a common ancestor class of T and
   * {@code source.getClass()}.
   *
   * @param source The object to copy.
   */
  public void cloneSharedFields(Object source) {
    Class<?> sourceClass = source.getClass();
    while (sourceClass.isAssignableFrom(clazz)) {
      sourceClass = sourceClass.getSuperclass();
    }
    while (!(sourceClass.equals(Object.class))) {
      for (Field field : sourceClass.getDeclaredFields()) {
        if (!(Modifier.isStatic(field.getModifiers()))) {
          setField(field, getField(source, field));
        }
      }
      sourceClass = sourceClass.getSuperclass();
    }
  }

  public T build() {
    T built;
    try {
      built = allocateInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    }
    synchronized (this) {
      for (Field f : fieldValues.keySet()) {
        setFieldInInstance(built, f, fieldValues.get(f));
      }
      for (Field f : explicitlyNullFields) {
        setFieldInInstance(built, f, null);
      }
    }
    return built;
  }
}
