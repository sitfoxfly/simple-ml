package org.simpleml.gen;

import java.util.Iterator;

/**
 * @author sitfoxfly
 */
public abstract class AbstractGenerator<T> implements Iterator<T> {

    @Override
    public final boolean hasNext() {
        return true;
    }

    @Override
    public abstract T next();

    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Generator has not got remove method");
    }
}
