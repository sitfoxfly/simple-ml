package org.simpleml.classify;

import org.simpleml.struct.ArrayVector;
import org.simpleml.struct.LabeledVector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mtkachenko
 */
public class TestUtil {

    private TestUtil() {
        throw new AssertionError("This is a 'static' class.");
    }

    public static List<LabeledVector> getSimpleTrainingData() {
        List<LabeledVector> trainingData = new ArrayList<LabeledVector>(5);

        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, -1, 0}), -1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{0, 1, 0}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{1, 0, 1}), 1));
        trainingData.add(new LabeledVector(new ArrayVector(new double[]{-1, -1, -1}), -1));

        return trainingData;
    }

}
