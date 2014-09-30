package org.simpleml.classify;

import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class DataUtils {

  public static final int DATA_SIZE = 3;

  public static List<LabeledVector> getSimpleTrainingData() {
    final List<LabeledVector> trainingData = new ArrayList<>(5);

    trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, -1, 0}), -1));
    trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
    trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, 1, 0}), 1));
    trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
    trainingData.add(new LabeledVector(new ArrayVector(new double[]{-1, -1, -1}), -1));

    return trainingData;
  }

  private DataUtils() {
    throw new AssertionError("This is a 'static' class.");
  }

}
