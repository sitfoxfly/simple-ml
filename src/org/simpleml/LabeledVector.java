package org.simpleml;

/**
 * @author rasmikun
 */
public class LabeledVector implements Vector {

    private Vector vector;
    private int label;

    public LabeledVector(Vector vector, int label) {
        this.vector = vector;
        this.label = label;
    }

    @Override
    public double get(int index) {
        return vector.get(index);
    }

    @Override
    public void set(int index, double value) {
        vector.set(index, value);
    }

    @Override
    public int size() {
        return vector.size();
    }

    @Override
    public double innerProduct(Vector vector) {
        return this.vector.innerProduct(vector);
    }

    @Override
    public void addToThis(Vector vector, double scalar) {
        this.vector.addToThis(vector, scalar);
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "[" + vector.toString() + ", " + label + "]";

    }
}
