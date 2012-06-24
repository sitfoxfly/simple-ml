package org.simpleml;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rasmikun
 */
public class SparseVectorTest {
    private Vector aVector = new ArrayVector(new double[]{1, 1, 1, 0, 0, 1});
    private Vector sVector;

    @Test
    public void testSetSparseVectorTest() throws Exception {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 1d);
        map.put(1, 1d);
        map.put(2, 1d);
        map.put(5, 1d);

        sVector = new SparseVector(map);

        Assert.assertEquals(sVector.size(), aVector.size());
        for (int i = 0; i < aVector.size(); i++) {
            Assert.assertEquals(sVector.get(i), aVector.get(i));
        }
    }

    @Test
    public void testSetIteratorSparseVectorText() throws Exception {
        ArrayList<IndexedValue> arrayList = new ArrayList<IndexedValue>();
        arrayList.add(new IndexedValue(0, 1d));
        arrayList.add(new IndexedValue(1, 1d));
        arrayList.add(new IndexedValue(2, 1d));
        arrayList.add(new IndexedValue(5, 1d));

        Vector otherSparseVector = new SparseVector(arrayList.iterator());

        Assert.assertEquals(otherSparseVector.size(), aVector.size());
        for (int i = 0; i < aVector.size(); i++) {
            Assert.assertEquals(aVector.get(i), otherSparseVector.get(i));
        }
    }

    @Test
    public void testInnerProductSparseVectorTest() throws Exception {
        Assert.assertEquals(sVector.innerProduct(aVector), aVector.innerProduct(aVector));
        Assert.assertEquals(sVector.innerProduct(sVector), aVector.innerProduct(aVector));
        Assert.assertEquals(sVector.innerProduct(sVector), aVector.innerProduct(sVector));
    }

    @Test
    public void testSumSparseVectorTest() throws Exception {
        sVector.addToThis(sVector, 2);
        aVector.addToThis(aVector, 2);
        for (int i = 0; i < sVector.size(); i++) {
            Assert.assertEquals(aVector.get(i), sVector.get(i));
        }
    }

}
