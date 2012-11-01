package org.simpleml.struct;

import org.simpleml.IndexedValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author rasmikun
 * @author sitfoxfly
 */
public class ArrayVector implements MutableVector {

    private double[] data;

    public ArrayVector(int dimension) {
        this.data = new double[dimension];
    }

    public ArrayVector(double[] data) {
        this.data = Arrays.copyOf(data, data.length);
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = reduceToZero(this.data[i]);
        }
    }

    public ArrayVector(Collection<IndexedValue> values, int dimension) {
        this.data = new double[dimension];
        for (IndexedValue value : values) {
            this.data[value.index] = reduceToZero(value.value);
        }
    }

    private void checkDimensions(int thatDim) {
        final int thisDim = getDimension();
        if (thisDim != thatDim) {
            throw new IllegalArgumentException("Dimensions of vectors are not equals: " + thatDim + " != " + thisDim);
        }
    }

    private double reduceToZero(double value) {
        if (Math.abs(value) < ZERO_EPSILON) {
            return ZERO;
        }
        return value;
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
        checkDimensions(thatVector.getDimension());
        Iterator<Entry> iterator = thatVector.sparseIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            final int index = entry.getIndex();
            data[index] = reduceToZero(data[index] + entry.getValue());
        }
    }

    @Override
    public void addToThis(Vector thatVector, double scalar) {
        checkDimensions(thatVector.getDimension());
        Iterator<Entry> iterator = thatVector.sparseIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            final int index = entry.getIndex();
            this.data[index] = reduceToZero(this.data[index] + entry.getValue() * scalar);
        }
    }

    @Override
    public int getDimension() {
        return data.length;
    }

    @Override
    public double innerProduct(Vector thatVector) {
        checkDimensions(thatVector.getDimension());
        double result = ZERO;
        Iterator<Entry> iterator = thatVector.sparseIterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            result += this.data[entry.getIndex()] * entry.getValue();
        }
        return result;
    }

    @Override
    public double getL2() {
        double result = 0d;
        for (double value : data) {
            result += value * value;
        }
        return Math.sqrt(result);
    }

    @Override
    public Iterator<Entry> sparseIterator() {
        return new ArrayVectorDenseIterator();
    }

    @Override
    public int sparseSize() {
        return data.length;
    }

    @Override
    public void scaleBy(double s) {
        for (int i = 0; i < data.length; i++) {
            data[i] *= s;
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

    }

    private class ArrayVectorDenseIterator implements Iterator<Entry> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < data.length;
        }

        @Override
        public Entry next() {
            final Entry result = new ArrayVectorEntry(index);
            index++;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

/*
    private class ArrayVectorSparseIterator implements Iterator<Entry> {

        private boolean hasNext = false;
        private int index = -1;

        private boolean nextNonZeroValue() {
            index++;
            while (data.length > index && Math.abs(data[index]) < ZERO_EPSILON) {
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
*/

}
