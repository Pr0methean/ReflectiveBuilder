package com.github.redstonevalley.util.reflect.builder;

/**
 * A {@link ReflectiveBuilder} in which fields of a class other than T can be set, as long as T has
 * fields of compatible types at the same byte offsets.
 */
public abstract class UncheckedReflectiveBuilder<T> extends ReflectiveBuilder<T> {

  public UncheckedReflectiveBuilder(Class<T> clazz) {
    super(clazz, true);
    // TODO Auto-generated constructor stub
  }
}
