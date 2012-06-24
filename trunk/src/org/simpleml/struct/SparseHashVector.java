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
public class SparseHashVector implements MutableVector, Vector {

    private static final double ZERO_VALUE = 0d;

    private TIntDoubleMap map;
    private int dimension;

    public SparseHashVector(TIntDoubleMap values, int dimension) {
        map = new TIntDoubleHashMap(values);
        this.dimension = dimension;
    }

    public SparseHashVector(Collection<IndexedValue> values, int dimension) {
        this(values.iterator(), dimension);
    }

    public SparseHashVector(Iterator<IndexedValue> values, int dimension) {
        map = new TIntDoubleHashMap();
        this.dimension = dimension;

        while (values.hasNext()) {
            IndexedValue value = values.next();
            map.put(value.getIndex(), value.getValue());
        }
    }

    public SparseHashVector(Map<Integer, Double> values, int dimension) {
        map = new TIntDoubleHashMap();
        this.dimension = dimension;

        for (Map.Entry<Integer, Double> entry : values.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public double get(int index) {
        if (index >= dimension) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " >= " + dimension);
        }
        if (index < 0) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " < 0");
        }
        if (map.containsKey(index)) {
            return map.get(index);
        } else {
            return ZERO_VALUE;
        }
    }

    @Override
    public void set(int index, double value) {
        if (index >= dimension) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " >= " + dimension);
        }
        if (index < 0) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " < 0");
        }
        map.put(index, value);
    }

    @Override
    public int size() {
        return this.dimension;
    }

    @Override
    public double innerProduct(Vector thatVector) {
        if (thatVector.size() != this.dimension) {
            throw new IllegalArgumentException("Illegal dimension of vectors: " + this.dimension + " != " + thatVector.size());
        }
        double result = 0d;
        TIntDoubleIterator iterator = map.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            result += iterator.value() * thatVector.get(iterator.key());
        }
        return result;
    }

    @Override
    public void addToThis(Vector thatVector) {
        TIntDoubleIterator iterator = this.map.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            iterator.setValue(iterator.value() + thatVector.get(iterator.key()));
        }
    }

    public void addToThis(Vector thatVector, double scalar) {
        if (thatVector.size() != dimension) {
            throw new IllegalArgumentException("Illegal dimension of vectors: " + dimension + " != " + thatVector.size());
        }

        TIntDoubleIterator iterator = this.map.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            iterator.setValue(iterator.value() + thatVector.get(iterator.key()) * scalar);
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

                    @Override
                    public void setValue(double value) {
                        if (innerIterator.key() != index) {
                            map.put(index, value);
                            return;
                        }
                        innerIterator.setValue(value);
                    }
                };
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public void product(final double scalar) {
        map.transformValues(new TDoubleFunction() {
            @Override
            public double execute(double v) {
                return v * scalar;
            }
        });
    }


}
