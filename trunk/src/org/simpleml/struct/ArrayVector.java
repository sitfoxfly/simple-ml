package org.simpleml.struct;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author rasmikun
 */
public class ArrayVector implements MutableVector {

    private double[] data;

    public ArrayVector(int n) {
        this.data = new double[n];
    }

    public ArrayVector(double[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    @Override
    public double get(int index) {
        return data[index];
    }

    @Override
    public void set(int index, double value) {
        data[index] = value;
    }

    @Override
    public void addToThis(Vector thatVector) {
        if (thatVector.size() != this.data.length) {
            throw new IllegalArgumentException("Illegal size of vectors: " + this.data.length + " != " + thatVector.size());
        }
        Iterator<Entry> iterator = thatVector.sparseIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            this.data[entry.getIndex()] += entry.getValue();
        }
    }

    @Override
    public void addToThis(Vector thatVector, double scalar) {
        if (thatVector.size() != this.data.length) {
            throw new IllegalArgumentException("Illegal size of vectors: " + this.data.length + " != " + thatVector.size());
        }
        Iterator<Entry> iterator = thatVector.sparseIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            this.data[entry.getIndex()] += entry.getValue() * scalar;
        }
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public double innerProduct(Vector thatVector) {
        if (thatVector.size() != this.data.length) {
            throw new IllegalArgumentException("Illegal size of vectors: " + this.data.length + " != " + thatVector.size());
        }
        double result = 0d;
        Iterator<Entry> iterator = thatVector.sparseIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            result += this.data[entry.getIndex()] * entry.getValue();
        }
        return result;
    }

    @Override
    public Iterator<Entry> sparseIterator() {
        return new ArrayVectorSparseIterator();
    }

    @Override
    public void product(double scalar) {
        for (int i = 0; i < data.length; i++) {
            data[i] *= scalar;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }

    private class ArrayVectorEntry implements Entry {

        private int index;

        public ArrayVectorEntry(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public double getValue() {
            return data[index];
        }

        @Override
        public void setValue(double value) {
            data[index] = value;
        }
    }

    private class ArrayVectorSparseIterator implements Iterator<Entry> {

        private boolean hasNext = false;
        private int index = -1;

        private boolean nextNonZeroValue() {
            index++;
            while (data.length > index && data[index] == 0d) {
                index++;
            }
            return data.length > index;
        }

        @Override
        public boolean hasNext() {
            if (!hasNext) {
                hasNext = nextNonZeroValue();
                return hasNext;
            }
            return true;
        }

        @Override
        public Entry next() {
            if (!hasNext) {
                if (!nextNonZeroValue()) {
                    throw new RuntimeException("");
                }
            }
            hasNext = false;
            return new ArrayVectorEntry(index);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
