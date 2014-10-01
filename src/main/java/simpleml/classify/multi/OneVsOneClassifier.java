package simpleml.classify.multi;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import simpleml.classify.Classifier;
import simpleml.classify.ConfidentBinaryClassifier;
import simpleml.struct.LabeledVector;
import simpleml.struct.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class OneVsOneClassifier implements Classifier {

  private static final int POSITIVE = 1;
  private static final int NEGATIVE = -1;

  private final BinaryClassifierTrainer trainer;

  private ConfidentBinaryClassifier[][] classifiers;
  private int[] labelIndex;

  public OneVsOneClassifier(BinaryClassifierTrainer trainer) {
    this.trainer = trainer;
  }

  public void train(List<LabeledVector> data) {
    final TIntObjectMap<List<LabeledVector>> index = new TIntObjectHashMap<>();
    for (LabeledVector lv : data) {
      final int label = lv.getLabel();
      if (!index.containsKey(label)) {
        index.put(label, new ArrayList<LabeledVector>());
      }
      index.get(label).add(lv);
    }

    labelIndex = index.keys();
    classifiers = new ConfidentBinaryClassifier[labelIndex.length][];

    for (int i = 0; i < labelIndex.length; i++) {
      classifiers[i] = new ConfidentBinaryClassifier[i];
      for (int j = 0; j < i; j++) {
        classifiers[i][j] = trainer.train(labeledAs(index.get(labelIndex[i]), POSITIVE), labeledAs(index.get(labelIndex[j]), NEGATIVE));
      }
    }
  }

  private List<LabeledVector> labeledAs(List<LabeledVector> data, int label) {
    final List<LabeledVector> result = new ArrayList<>();
    for (LabeledVector lv : data) {
      result.add(new LabeledVector(lv.getInnerVector(), label));
    }
    return result;
  }

  @Override
  public int classify(Vector vector) {
    final double[][] confidences = new double[classifiers.length][];
    for (int i = 0; i < classifiers.length; i++) {
      confidences[i] = new double[i];
      for (int j = 0; j < i; j++) {
        confidences[i][j] = classifiers[i][j].classifyWithConfidence(vector);
      }
    }

    final double[] confidenceForLabel = new double[confidences.length];
    for (int i = 0; i < confidences.length; i++) {
      confidenceForLabel[i] = 0.0;
      for (int j = 0; j < confidences.length; j++) {
        if (i == j) {
          continue;
        }
        if (i < j) {
          confidenceForLabel[i] += -confidences[j][i];
        } else {
          confidenceForLabel[i] += confidences[i][j];
        }
      }
    }

    double maxVal = confidenceForLabel[0];
    int maxIndex = 0;
    for (int i = 1; i < confidenceForLabel.length; i++) {
      if (maxVal < confidenceForLabel[i]) {
        maxVal = confidenceForLabel[i];
        maxIndex = i;
      }
    }

    return labelIndex[maxIndex];
  }

}
