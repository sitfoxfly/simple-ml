package org.simpleml.struct;

import gnu.trove.iterator.TIntDoubleIterator;
import junit.framework.Assert;
import org.junit.Test;
import org.simpleml.struct.map.TIntDoubleTreeMap;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author sitfoxfly
 */
public class TIntDoubleTreeMapTest {

    @Test
    public void testPut() throws Exception {
        final Random rand = new Random(1);
        final int numOfPuts = 1000;

        TIntDoubleTreeMap map = new TIntDoubleTreeMap();
        Set<Integer> keys = new TreeSet<Integer>();
        for (int i = 0; i < numOfPuts; i++) {
            final int key = rand.nextInt();
            map.put(key, i);
            keys.add(key);
            TIntDoubleIterator keyValIter = map.iterator();
            final Iterator<Integer> keysIter = keys.iterator();
            Assert.assertEquals(keys.size(), map.size());
            while (keyValIter.hasNext()) {
                keyValIter.advance();
                Assert.assertEquals(keyValIter.key(), (int) keysIter.next());
            }
        }
    }

    @Test
    public void testRemove() {
        final double defaultValue = 1.0;
        TIntDoubleTreeMap map = new TIntDoubleTreeMap();
        map.remove(1);
        map.put(1, defaultValue);
        map.remove(1);
        Assert.assertEquals(0, map.size());
        map.put(1, defaultValue);
        map.put(2, defaultValue);
        map.remove(3);
        Assert.assertEquals(2, map.size());
        map.remove(2);
        Assert.assertEquals(1, map.size());
        map.remove(1);
        Assert.assertEquals(0, map.size());
        map.remove(0);
        Assert.assertEquals(0, map.size());
    }

}
