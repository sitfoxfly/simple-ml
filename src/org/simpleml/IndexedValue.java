package org.simpleml;

/**
 * Created with IntelliJ IDEA.
 * User: rasmi
 * Date: 6/24/12
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexedValue {
    private int index;
    private double value;

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
