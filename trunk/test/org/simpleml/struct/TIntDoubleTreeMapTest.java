package org.simpleml.struct;

import gnu.trove.iterator.TIntDoubleIterator;
import junit.framework.Assert;
import org.junit.Test;

import java.util.*;

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
}
