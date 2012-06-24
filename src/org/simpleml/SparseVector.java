package org.simpleml;

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;

/**
 * @author rasmikun
 */
public class SparseVector implements Vector {
    private TIntDoubleMap map;
    private int vector_size = -1;

    private int[] xrange(int from, int to) {
        int[] result = new int[to - from];

        for (int i = from; i < to; i++) {
            result[i] = i;
        }

        return result;
    }

    SparseVector(double[] vector) {
        map = new TIntDoubleHashMap(xrange(0, vector.length), vector);
    }

    @Override
    public double get(int index) {
        if (vector_size < index)
            throw new IllegalArgumentException("Illegal vector index: " + vector_size + " < " + index);
        if (map.containsKey(index)) {
            return map.get(index);
        } else {
            return 0d;
        }
    }

    @Override
    public void set(int index, double value) {
        if (index < 0) throw new IllegalArgumentException("Illegal vector index: " + index + " < 0");
        map.put(index, value);
        if (vector_size < index) {
            vector_size = index + 1;
        }
    }

    @Override
    public int size() {
        return vector_size;
    }

    @Override
    public double innerProduct(Vector vector) {
        if (vector.size() != vector_size) {
            throw new IllegalArgumentException("Illegal size of vectors: " + vector_size + " != " + vector.size());
        }
        double result = 0d;
        for (int i = 0; i < vector_size; i++) {
            if (map.containsKey(i)) {
                result += map.get(i) * vector.get(i);
            }
        }
        return result;
    }

    @Override
    public void addToThis(Vector vector, double scalar) {
        if (vector.size() != vector_size) {
            throw new IllegalArgumentException("Illegal size of vectors: " + vector_size + " != " + vector.size());
        }
        for (int i = 0; i < vector_size; i++) {
            map.put(i, this.get(i) + vector.get(i) * scalar);
        }
    }
}
