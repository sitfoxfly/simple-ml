package simpleml.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author sitfoxfly
 */
public class EachInstanceGenerator<T> implements Iterable<Iterable<T>> {

  private final Iterable<T> emptyIterable = Collections.unmodifiableList(new ArrayList<T>(0));

  private Iterable<T> iterable;

  public EachInstanceGenerator(Iterable<T> iterable) {
    this.iterable = iterable;
  }

  @Override
  public Iterator<Iterable<T>> iterator() {
    return new AbstractGenerator<Iterable<T>>() {

      private Iterator<T> iterator = iterable.iterator();

      @Override
      public Iterable<T> next() {
        if (!iterator.hasNext()) {
          iterator = iterable.iterator();
          if (!iterator.hasNext()) {
            return emptyIterable;
          }
        }
        return new SingleElement<>(iterator.next());
      }
    };
  }

  private static class SingleElement<T> implements Iterable<T> {

    private T element;

    public SingleElement(T element) {
      this.element = element;
    }

    @Override
    public Iterator<T> iterator() {
      return new Iterator<T>() {

        private boolean hasNext = true;

        @Override
        public boolean hasNext() {
          return hasNext;
        }

        @Override
        public T next() {
          if (!hasNext) {
            throw new IllegalStateException();
          }
          hasNext = false;
          return element;
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

  }

}
