package org.simpleml;

/**
 * @author rasmikun
 */
public interface Vector {

    public double get(int index);

    public void set(int index, double value);

    public int size();

    public double innerProduct(Vector vector);

    public void addToThis(Vector vector, double scalar);

}
