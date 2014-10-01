package simpleml.struct;

import org.junit.Assert;
import org.junit.Test;
import simpleml.TestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rasmikun
 */
public class SparseHashVectorTest {

  // TODO: Code more comfortable testing env (Vectors initialization)
  private MutableVector arrayVector = new ArrayVector(new double[]{1, 1, 1, 0, 0, 1});
  private MutableVector sparseVector;

  {
    final Map<Integer, Double> map = new HashMap<>();
    map.put(0, 1d);
    map.put(1, 1d);
    map.put(2, 1d);
    map.put(5, 1d);
    sparseVector = new SparseHashVector(map, 6);
  }

  @Test
  public void testSetSparseVectorTest() throws Exception {
    Assert.assertEquals(sparseVector.getDimension(), arrayVector.getDimension());
    for (int i = 0; i < arrayVector.getDimension(); i++) {
      Assert.assertEquals(sparseVector.get(i), arrayVector.get(i), TestUtils.DELTA);
    }
  }

  @Test
  public void testSetIteratorSparseVectorText() throws Exception {
    final ArrayList<IndexedValue> arrayList = new ArrayList<>();
    arrayList.add(new IndexedValue(0, 1d));
    arrayList.add(new IndexedValue(1, 1d));
    arrayList.add(new IndexedValue(2, 1d));
    arrayList.add(new IndexedValue(5, 1d));

    final Vector otherSparseVector = new SparseHashVector(arrayList.iterator(), 6);

    Assert.assertEquals(otherSparseVector.getDimension(), arrayVector.getDimension());
    for (int i = 0; i < arrayVector.getDimension(); i++) {
      Assert.assertEquals(arrayVector.get(i), otherSparseVector.get(i), TestUtils.DELTA);
    }
  }

  @Test
  public void testInnerProductSparseVectorTest() throws Exception {
    Assert.assertEquals(sparseVector.innerProduct(arrayVector), arrayVector.innerProduct(arrayVector), TestUtils.DELTA);
    Assert.assertEquals(sparseVector.innerProduct(sparseVector), arrayVector.innerProduct(arrayVector), TestUtils.DELTA);
    Assert.assertEquals(sparseVector.innerProduct(sparseVector), arrayVector.innerProduct(sparseVector), TestUtils.DELTA);
  }

  @Test
  public void testSumSparseVectorTest() throws Exception {
    sparseVector.addToThis(sparseVector, 2);
    arrayVector.addToThis(arrayVector, 2);
    for (int i = 0; i < sparseVector.getDimension(); i++) {
      Assert.assertEquals(arrayVector.get(i), sparseVector.get(i), TestUtils.DELTA);
    }
  }

  @Test
  public void testGetL2() throws Exception {
    final Vector v = new SparseHashVector(new double[]{0, 0, 0});
    Assert.assertEquals(0d, v.getL2(), TestUtils.DELTA);

    final Vector v2 = new SparseHashVector(new double[]{0, 0, 2});
    Assert.assertEquals(2d, v2.getL2(), TestUtils.DELTA);

    final Vector v3 = new SparseHashVector(new double[]{4, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 2, 2, 2, 4, 4});
    Assert.assertEquals(8d, v3.getL2(), TestUtils.DELTA);
  }

}
