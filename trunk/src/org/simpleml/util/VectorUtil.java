package org.simpleml.util;

import org.simpleml.struct.Vector;

import java.util.Iterator;

/**
 * @author sitfoxfly
 */
public class VectorUtil {

    private VectorUtil() {
        throw new AssertionError("This is a static class!");
    }

    public static Vector immutableVector(Vector vector) {
        return new ImmutableVector(vector);
    }

    private static class ImmutableVector implements Vector {

        private Vector vector;

        public ImmutableVector(Vector vector) {
            this.vector = vector;
        }

        @Override
        public double get(int index) {
            return vector.get(index);
        }

        @Override
        public int getDimension() {
            return vector.getDimension();
        }

        @Override
        public double innerProduct(Vector thatVector) {
            return this.vector.innerProduct(thatVector);
        }

        @Override
        public double getL2() {
            return vector.getL2();
        }

        @Override
        public int sparseSize() {
            return vector.sparseSize();
        }

        @Override
        public Iterator<Entry> sparseIterator() {
            return new Iterator<Entry>() {

                private Iterator<Entry> innerIterator = vector.sparseIterator();

                @Override
                public boolean hasNext() {
                    return innerIterator.hasNext();
                }

                @Override
                public Entry next() {
                    return new Entry() {

                        private Entry innerEntry = innerIterator.next();

                        @Override
                        public int getIndex() {
                            return innerEntry.getIndex();
                        }

                        @Override
                        public double getValue() {
                            return innerEntry.getValue();
                        }
                    };
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    public static String toSparseString(Vector v) {
        StringBuilder sb = new StringBuilder("V = [");
        final Iterator<Vector.Entry> sparseIterator = v.sparseIterator();
        while (sparseIterator.hasNext()) {
            final Vector.Entry entry = sparseIterator.next();
            sb.append(entry.getIndex()).append(": ").append(entry.getValue()).append("; ");
        }
        sb.append("]");
        return sb.toString();
    }
}
