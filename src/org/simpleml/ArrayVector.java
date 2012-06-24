package org.simpleml;

import java.util.Arrays;

/**
 * @author rasmikun
 */
public class ArrayVector implements Vector {

    private double[] vector;

    public ArrayVector(double[] vector) {
        this.vector = vector;
    }

    @Override
    public double get(int index) {
        if (vector.length <= index) {
            throw new IllegalArgumentException("Illegal index: " + index + " >= " + this.vector.length);
        }
        if (index < 0) {
            throw new IllegalArgumentException("Illegal index: " + index + " < 0");
        }
        return vector[index];
    }

    @Override
    public void set(int index, double value) {
        if (vector.length <= index) {
            throw new IllegalArgumentException("Illegal index: " + index + " >= " + this.vector.length);
        }
        if (index < 0) {
            throw new IllegalArgumentException("Illegal index: " + index + " < 0");
        }
        vector[index] = value;
    }

    @Override
    public int size() {
        return vector.length;
    }

    @Override
    public double innerProduct(Vector vector) {
        if (vector.size() != this.vector.length) {
            throw new IllegalArgumentException("Illegal size of vectors: " + this.vector.length + " != " + vector.size());
        }
        double result = 0d;
        for (int i = 0; i < this.vector.length; i++) {
            result += this.vector[i] * vector.get(i);
        }
        return result;
    }

    @Override
    public void addToThis(Vector vector, double scalar) {
        if (vector.size() != this.vector.length) {
            throw new IllegalArgumentException("Illegal size of vectors: " + this.vector.length + " != " + vector.size());
        }
        for (int i = 0; i < this.vector.length; i++) {
            this.vector[i] += vector.get(i) * scalar;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(vector);
    }
}
