package org.simpleml.struct;

import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import org.simpleml.IndexedValue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author rasmikun
 */
public class SparseHashVector implements MutableVector {

  private TIntDoubleMap map;
  private int dimension;

  public SparseHashVector(Vector v) {
    map = new TIntDoubleHashMap();
    dimension = v.getDimension();
    final Iterator<Entry> sparseIterator = v.sparseIterator();
    while (sparseIterator.hasNext()) {
      final Entry entry = sparseIterator.next();
      if (Math.abs(entry.getValue()) < ZERO_EPSILON) {
        continue;
      }
      map.put(entry.getIndex(), entry.getValue());
    }
  }

  public SparseHashVector(int dimension) {
    map = new TIntDoubleHashMap();
    this.dimension = dimension;
  }

  public SparseHashVector(double[] values) {
    this.dimension = values.length;
    map = new TIntDoubleHashMap();
    for (int i = 0; i < values.length; i++) {
      if (Math.abs(values[i]) < ZERO_EPSILON) {
        continue;
      }
      map.put(i, values[i]);
    }
  }

  public SparseHashVector(TIntDoubleMap values, int dimension) {
    this.dimension = dimension;
    map = new TIntDoubleHashMap(values);
  }

  public SparseHashVector(Collection<IndexedValue> values, int dimension) {
    this.dimension = dimension;
    map = new TIntDoubleHashMap(values.size());

    for (IndexedValue value : values) {
      checkIndex(value.getIndex());
      map.put(value.getIndex(), value.getValue());
    }
  }

  public SparseHashVector(Iterator<IndexedValue> values, int dimension) {
    this.dimension = dimension;
    map = new TIntDoubleHashMap();

    while (values.hasNext()) {
      IndexedValue value = values.next();
      map.put(value.getIndex(), value.getValue());
    }
  }

  public SparseHashVector(Map<Integer, Double> values, int dimension) {
    this.dimension = dimension;
    map = new TIntDoubleHashMap();

    for (Map.Entry<Integer, Double> entry : values.entrySet()) {
      map.put(entry.getKey(), entry.getValue());
    }
  }

  private void checkDimensions(int thatDim) {
    final int thisDim = getDimension();
    if (thisDim != thatDim) {
      throw new IllegalArgumentException("Dimensions of vectors are not equals: " + thatDim + " != " + thisDim);
    }
  }

  private void checkIndex(int index) {
    if (index >= dimension) {
      throw new IllegalArgumentException("Illegal index: " + index + " >= " + dimension);
    } else if (index < 0) {
      throw new IllegalArgumentException("Illegal index: " + index + " < 0");
    }
  }

  @Override
  public double get(int index) {
    checkIndex(index);
    if (map.containsKey(index)) {
      return map.get(index);
    } else {
      return ZERO;
    }
  }

  @Override
  public void set(int index, double value) {
    checkIndex(index);
    if (Math.abs(value) < ZERO_EPSILON) {
      return;
    }
    map.put(index, value);
  }

  @Override
  public int getDimension() {
    return this.dimension;
  }

  @Override
  public double innerProduct(Vector thatVector) {
    checkDimensions(thatVector.getDimension());
    double result = ZERO;
    if (sparseSize() <= thatVector.sparseSize()) {
      final TIntDoubleIterator iterator = map.iterator();
      while (iterator.hasNext()) {
        iterator.advance();
        result += iterator.value() * thatVector.get(iterator.key());
      }
    } else {
      Iterator<Entry> iterator = thatVector.sparseIterator();
      while (iterator.hasNext()) {
        final Entry entry = iterator.next();
        result += map.get(entry.getIndex()) * entry.getValue();
      }
    }
    return result;
  }

  @Override
  public double getL2() {
    double result = ZERO;
    final TIntDoubleIterator iterator = map.iterator();
    while (iterator.hasNext()) {
      iterator.advance();
      double value = iterator.value();
      result += value * value;
    }
    return Math.sqrt(result);
  }

  @Override
  public void addToThis(Vector thatVector) {
    checkDimensions(thatVector.getDimension());
    final Iterator<Entry> iterator = thatVector.sparseIterator();
    while (iterator.hasNext()) {
      final Entry entry = iterator.next();
      final int index = entry.getIndex();
      double newValue = entry.getValue() + this.map.get(index);
      if (Math.abs(newValue) < ZERO_EPSILON) {
        this.map.remove(index);
      } else {
        this.map.put(index, newValue);
      }
    }
  }

  public void addToThis(Vector thatVector, double scalar) {
    checkDimensions(thatVector.getDimension());
    if (scalar == ZERO) {
      return;
    }
    final Iterator<Entry> iterator = thatVector.sparseIterator();
    while (iterator.hasNext()) {
      final Entry entry = iterator.next();
      final int index = entry.getIndex();
      double newValue = entry.getValue() * scalar + map.get(index);
      if (Math.abs(newValue) < ZERO_EPSILON) {
        map.remove(index);
      } else {
        map.put(index, newValue);
      }
    }
  }

  @Override
  public Iterator<Entry> sparseIterator() {
    return new Iterator<Entry>() {

      private TIntDoubleIterator innerIterator = map.iterator();

      @Override
      public boolean hasNext() {
        return innerIterator.hasNext();
      }

      @Override
      public Entry next() {
        innerIterator.advance();
        return new Entry() {

          private int index = innerIterator.key();

          @Override
          public int getIndex() {
            return index;
          }

          @Override
          public double getValue() {
            if (innerIterator.key() == index) {
              return innerIterator.value();
            }
            return map.get(index);
          }
        };
      }

      @Override
      public void remove() {
        innerIterator.remove();
      }
    };
  }

  @Override
  public int sparseSize() {
    return map.size();
  }

  @Override
  public void scaleBy(final double s) {
    map.transformValues(new TDoubleFunction() {
      @Override
      public double execute(double v) {
        return v * s;
      }
    });
  }


}
