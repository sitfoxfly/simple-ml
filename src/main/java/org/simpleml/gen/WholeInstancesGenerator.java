package org.simpleml.gen;

import java.util.Iterator;

/**
 * @author sitfoxfly
 */
public class WholeInstancesGenerator<T> implements Iterable<Iterable<T>> {

  private Iterable<T> iterable;

  public WholeInstancesGenerator(Iterable<T> iterable) {
    this.iterable = iterable;
  }

  @Override
  public Iterator<Iterable<T>> iterator() {
    return new AbstractGenerator<Iterable<T>>() {
      @Override
      public Iterable<T> next() {
        return iterable;
      }
    };
  }
}
