package org.simpleml;

/**
 * @author rasmikun
 */
public final class IndexedValue {

    public final int index;
    public final double value;

    public IndexedValue(int index, double value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public double getValue() {
        return value;
    }

}
