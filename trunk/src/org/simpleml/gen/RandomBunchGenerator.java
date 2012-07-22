package org.simpleml.gen;

import java.util.*;

/**
 * @author sitfoxfly
 */
public class RandomBunchGenerator<T> implements Iterable<Iterable<T>> {

    private static final int DEFAULT_SEED = 0;

    private List<T> instances;
    private int bunchSize;
    private long seed;

    public RandomBunchGenerator(int bunchSize, List<T> instances, long seed) {
        if (bunchSize >= instances.size()) {
            throw new IllegalArgumentException("Illegal bunchSize!");
        }
        this.bunchSize = bunchSize;
        this.instances = new ArrayList<T>(instances);
        this.seed = seed;
    }

    public RandomBunchGenerator(int bunchSize, List<T> instances) {
        this(bunchSize, instances, DEFAULT_SEED);
    }

    @Override
    public Iterator<Iterable<T>> iterator() {
        return new AbstractGenerator<Iterable<T>>() {

            private final Random random = new Random(seed);

            @Override
            public Iterable<T> next() {
                Collections.shuffle(instances, random);
                return Collections.unmodifiableList(instances.subList(0, bunchSize));
            }
        };
    }
}
