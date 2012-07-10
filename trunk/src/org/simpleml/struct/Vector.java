package org.simpleml.struct;

import java.util.Iterator;

/**
 * @author sitfoxfly
 */
public interface Vector {

    public static final double ZERO_EPSILON = 1E-12;
    public static final double ZERO = 0D;

    public double get(int index);

    public int getDimension();

    public Iterator<Entry> sparseIterator();

    public int sparseSize();

    public double innerProduct(Vector thatVector);

    public double getL2();

    public interface Entry {

        public int getIndex();

        public double getValue();

    }

}
