package org.simpleml.utils;

import org.simpleml.IndexedValue;
import org.simpleml.classify.ext.LoadException;
import org.simpleml.struct.Vector;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class VectorUtils {

  public static void save(Vector vector, DataOutput dataOut) throws IOException {
    dataOut.writeInt(vector.getDimension());
    dataOut.writeInt(vector.sparseSize());
    final Iterator<Vector.Entry> sparseIterator = vector.sparseIterator();
    while (sparseIterator.hasNext()) {
      final Vector.Entry entry = sparseIterator.next();
      dataOut.writeInt(entry.getIndex());
      dataOut.writeDouble(entry.getValue());
    }
  }

  public static Vector load(Class<? extends Vector> clazz, DataInput dataIn) throws IOException, LoadException {
    final int dimension = dataIn.readInt();
    final int sparseSize = dataIn.readInt();
    final List<IndexedValue> values = new ArrayList<>(sparseSize);
    for (int i = 0; i < sparseSize; i++) {
      final int index = dataIn.readInt();
      final double value = dataIn.readDouble();
      values.add(new IndexedValue(index, value));
    }
    Vector vector;
    try {
      final Constructor<? extends Vector> constructor = clazz.getConstructor(Collection.class, int.class);
      vector = constructor.newInstance(values, dimension);
    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
      throw new LoadException(e);
    }

    return vector;
  }

  public static boolean equals(Vector v1, Vector v2) {
    if (v1.getDimension() != v2.getDimension()) {
      return false;
    }
    final int n = v1.getDimension();
    for (int i = 0; i < n; i++) {
      if (v1.get(i) != v2.get(i)) {
        return false;
      }
    }
    return true;
  }

  public static Vector immutableVector(Vector vector) {
    return new ImmutableVector(vector);
  }

  private static class ImmutableVector implements Vector {

    private Vector vector;

    public ImmutableVector(Vector vector) {
      this.vector = vector;
    }

    @Override
    public double get(int index) {
      return vector.get(index);
    }

    @Override
    public int getDimension() {
      return vector.getDimension();
    }

    @Override
    public double innerProduct(Vector thatVector) {
      return this.vector.innerProduct(thatVector);
    }

    @Override
    public double getL2() {
      return vector.getL2();
    }

    @Override
    public int sparseSize() {
      return vector.sparseSize();
    }

    @Override
    public Iterator<Entry> sparseIterator() {
      return new Iterator<Entry>() {

        private Iterator<Entry> innerIterator = vector.sparseIterator();

        @Override
        public boolean hasNext() {
          return innerIterator.hasNext();
        }

        @Override
        public Entry next() {
          return new Entry() {

            private Entry innerEntry = innerIterator.next();

            @Override
            public int getIndex() {
              return innerEntry.getIndex();
            }

            @Override
            public double getValue() {
              return innerEntry.getValue();
            }
          };
        }

        @Override
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  public static String toSparseString(Vector v) {
    final StringBuilder sb = new StringBuilder("V = [");
    final Iterator<Vector.Entry> sparseIterator = v.sparseIterator();
    while (sparseIterator.hasNext()) {
      final Vector.Entry entry = sparseIterator.next();
      sb.append(entry.getIndex()).append(": ").append(entry.getValue()).append("; ");
    }
    sb.append("]");
    return sb.toString();
  }

  private VectorUtils() {
    throw new AssertionError("This is a static class!");
  }

}
