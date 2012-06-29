package org.simpleml.struct;

import junit.framework.Assert;
import org.junit.Test;
import org.simpleml.IndexedValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rasmikun
 */
public class SparseVectorTest {

    private MutableVector arrayVector = new ArrayVector(new double[]{1, 1, 1, 0, 0, 1});
    private MutableVector sparseVector;

    @Test
    public void testSetSparseVectorTest() throws Exception {
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(0, 1d);
        map.put(1, 1d);
        map.put(2, 1d);
        map.put(5, 1d);

        sparseVector = new SparseHashVector(map, 6);

        Assert.assertEquals(sparseVector.size(), arrayVector.size());
        for (int i = 0; i < arrayVector.size(); i++) {
            Assert.assertEquals(sparseVector.get(i), arrayVector.get(i));
        }
    }

    @Test
    public void testSetIteratorSparseVectorText() throws Exception {
        ArrayList<IndexedValue> arrayList = new ArrayList<IndexedValue>();
        arrayList.add(new IndexedValue(0, 1d));
        arrayList.add(new IndexedValue(1, 1d));
        arrayList.add(new IndexedValue(2, 1d));
        arrayList.add(new IndexedValue(5, 1d));

        Vector otherSparseVector = new SparseHashVector(arrayList.iterator(), 6);

        Assert.assertEquals(otherSparseVector.size(), arrayVector.size());
        for (int i = 0; i < arrayVector.size(); i++) {
            Assert.assertEquals(arrayVector.get(i), otherSparseVector.get(i));
        }
    }

    @Test
    public void testInnerProductSparseVectorTest() throws Exception {
        Assert.assertEquals(sparseVector.innerProduct(arrayVector), arrayVector.innerProduct(arrayVector));
        Assert.assertEquals(sparseVector.innerProduct(sparseVector), arrayVector.innerProduct(arrayVector));
        Assert.assertEquals(sparseVector.innerProduct(sparseVector), arrayVector.innerProduct(sparseVector));
    }

    @Test
    public void testSumSparseVectorTest() throws Exception {
        sparseVector.addToThis(sparseVector, 2);
        arrayVector.addToThis(arrayVector, 2);
        for (int i = 0; i < sparseVector.size(); i++) {
            Assert.assertEquals(arrayVector.get(i), sparseVector.get(i));
        }
    }

}
