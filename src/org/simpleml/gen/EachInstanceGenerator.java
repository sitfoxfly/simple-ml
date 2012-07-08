package org.simpleml.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author sitfoxfly
 */
public class EachInstanceGenerator<T> implements Iterable<Iterable<T>> {

    private final Iterable<T> emptyIterable = Collections.unmodifiableList(new ArrayList<T>(0));

    private Iterable<T> iterable;

    public EachInstanceGenerator(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    @Override
    public Iterator<Iterable<T>> iterator() {
        return new AbstractGenerator<Iterable<T>>() {

            private Iterator<T> iterator = iterable.iterator();

            @Override
            public Iterable<T> next() {
                if (!iterator.hasNext()) {
                    iterator = iterable.iterator();
                    if (!iterator.hasNext()) {
                        return emptyIterable;
                    }
                }
                final LinkedList<T> list = new LinkedList<T>();
                list.add(iterator.next());
                return list;
            }
        };
    }
}
