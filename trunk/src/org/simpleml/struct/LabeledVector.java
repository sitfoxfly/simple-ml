package org.simpleml.struct;

import java.util.Iterator;

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
    public Iterator<Vector.Entry> sparseIterator() {
        return vector.sparseIterator();
    }

    @Override
    public double innerProduct(Vector thatVector) {
        return vector.innerProduct(thatVector);
    }

    @Override
    public int getDimension() {
        return vector.getDimension();
    }

    @Override
    public int sparseSize() {
        return vector.sparseSize();
    }

    public Vector getInnerVector() {
        return vector;
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
