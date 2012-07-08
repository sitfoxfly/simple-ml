package org.simpleml.struct;

/**
 * @author rasmikun
 */
public interface MutableVector extends Vector {

    public void set(int index, double value);

    public void addToThis(Vector thatVector);

    public void addToThis(Vector thatVector, double scalar);

    public void scaleBy(double s);

}
