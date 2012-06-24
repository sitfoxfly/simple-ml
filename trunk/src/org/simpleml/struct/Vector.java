package org.simpleml.struct;

import java.util.Iterator;

/**
 * @author sitfoxfly
 */
public interface Vector {

    public double get(int index);

    public int size();

    public Iterator<Entry> sparseIterator();

    public double innerProduct(Vector thatVector);

    public interface Entry {

        public int getIndex();

        public double getValue();

        public void setValue(double value);

    }

}
