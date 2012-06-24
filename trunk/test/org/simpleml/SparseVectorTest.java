package org.simpleml;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author rasmikun
 */
public class SparseVectorTest {
    private Vector aVector = new ArrayVector(new double[]{1, 1, 1, 0, 0, 1});
    private Vector sVector = new SparseVector(new double[]{1, 1, 1});

    @Test
    public void testSetSparseVectorTest() throws Exception {
        sVector.set(5, 1);

        Assert.assertEquals(sVector.size(), aVector.size());
        for (int i = 0; i < aVector.size(); i++) {
            Assert.assertEquals(sVector.get(i), aVector.get(i));
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
