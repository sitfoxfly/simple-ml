package org.simpleml;

import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.Iterator;
import java.util.Map;

/**
 * @author rasmikun
 */
public class SparseVector implements Vector {
    private TIntDoubleMap map;
    private int vectorSize = -1;

    SparseVector(TIntDoubleMap map) {
        this.map = new TIntDoubleHashMap(map);
        TIntDoubleIterator iterator = map.iterator();

        while (iterator.hasNext()) {
            int key = iterator.key();
            if (vectorSize < key) {
                vectorSize = key + 1;
            }
        }
    }

    SparseVector(Iterator<IndexedValue> iterator) {
        this.map = new TIntDoubleHashMap();

        while (iterator.hasNext()) {
            IndexedValue value = iterator.next();
            if (vectorSize < value.getIndex()) {
                vectorSize = value.getIndex() + 1;
            }
            this.map.put(value.getIndex(), value.getValue());
        }
    }

    SparseVector(Map<Integer, Double> map) {
        this.map = new TIntDoubleHashMap();

        Iterator<Integer> keyIterator = map.keySet().iterator();
        Iterator<Double> valIterator = map.values().iterator();

        while (keyIterator.hasNext()) {
            int key = keyIterator.next();
            if (vectorSize < key) {
                vectorSize = key + 1;
            }
            this.map.put(key, valIterator.next());
        }
    }


    @Override
    public double get(int index) {
        if (index >= vectorSize) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " >= " + vectorSize);
        }
        if (index < 0) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " < 0");
        }
        if (map.containsKey(index)) {
            return map.get(index);
        } else {
            return 0d;
        }
    }

    @Override
    public void set(int index, double value) {
        if (index >= vectorSize) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " >= " + vectorSize);
        }
        if (index < 0) {
            throw new IllegalArgumentException("Illegal vector index: " + index + " < 0");
        }
        map.put(index, value);
    }

    @Override
    public int size() {
        return vectorSize;
    }

    @Override
    public double innerProduct(Vector vector) {
        if (vector.size() != vectorSize) {
            throw new IllegalArgumentException("Illegal size of vectors: " + vectorSize + " != " + vector.size());
        }
        double result = 0d;
        for (int i = 0; i < vectorSize; i++) {
            if (map.containsKey(i)) {
                result += map.get(i) * vector.get(i);
            }
        }
        return result;
    }

    @Override
    public void addToThis(Vector vector, double scalar) {
        if (vector.size() != vectorSize) {
            throw new IllegalArgumentException("Illegal size of vectors: " + vectorSize + " != " + vector.size());
        }
        for (int i = 0; i < vectorSize; i++) {
            map.put(i, this.get(i) + vector.get(i) * scalar);
        }
    }
}
