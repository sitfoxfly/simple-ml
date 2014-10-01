package simpleml.read;

import simpleml.classify.ConfidentBinaryClassifier;
import simpleml.classify.PegasosSVM;
import simpleml.classify.multi.BinaryClassifierTrainer;
import simpleml.classify.multi.OneVsOneClassifier;
import simpleml.classify.notify.progress.TrainingProgressPrinter;
import simpleml.struct.IndexedValue;
import simpleml.struct.LabeledVector;
import simpleml.struct.SparseHashVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class LibSvmDataset {

  private final File file;
  private final int numOfFeatures;

  public LibSvmDataset(File file, int numOfFeatures) {
    this.file = file;
    this.numOfFeatures = numOfFeatures;
  }

  private String cutPlus(String s) {
    if (s.startsWith("+")) {
      return s.substring(1);
    }
    return s;
  }

  public List<LabeledVector> readAll() throws IOException {
    List<LabeledVector> result = new LinkedList<>();
    BufferedReader reader = new BufferedReader(new FileReader(this.file));

    String line;
    while (null != (line = reader.readLine())) {
      line = line.trim();
      String[] vals = line.split("[\\s:]");
      int label = Integer.valueOf(cutPlus(vals[0]));
      List<IndexedValue> values = new LinkedList<>();
      for (int i = 1; i < vals.length; i += 2) {
        values.add(new IndexedValue(Integer.parseInt(vals[i]), Double.parseDouble(vals[i + 1])));
      }
      result.add(new LabeledVector(new SparseHashVector(values, numOfFeatures), label));
    }

    return result;
  }

  public static void main(String[] args) throws IOException {
    final int numOfFeatures = 11;

    LibSvmDataset trainDataSet = new LibSvmDataset(new File("dataset/svmguide4"), numOfFeatures);
    LibSvmDataset testDataSet = new LibSvmDataset(new File("dataset/svmguide4.t"), numOfFeatures);


    OneVsOneClassifier classifier = new OneVsOneClassifier(new BinaryClassifierTrainer() {
      @Override
      public ConfidentBinaryClassifier train(List<LabeledVector> data1, List<LabeledVector> data2) {
        PegasosSVM cls = new PegasosSVM(numOfFeatures);
        cls.addTrainingProgressListener(new TrainingProgressPrinter());
        cls.setLambda(1.0E-4);
        cls.setNumIteration(2500);
                /*LinearPerceptron cls = new LinearPerceptron(numOfFeatures);
                cls.addTrainingProgressListener(new TrainingProgressPrinter());
                cls.setNumIteration(2000);*/
        List<LabeledVector> data = new ArrayList<>(data1.size() + data2.size());
        data.addAll(data1);
        data.addAll(data2);
        Collections.shuffle(data);
        cls.train(data);
        return cls;
      }
    });

    classifier.train(trainDataSet.readAll());

    final List<LabeledVector> test = testDataSet.readAll();

    int correct;

    correct = 0;
    for (LabeledVector vector : test) {
      int predictedLabel = classifier.classify(vector.getInnerVector());
      if (predictedLabel == vector.getLabel()) {
        correct++;
      }
    }

    System.out.println("A(Per) = " + ((double) correct / test.size()));

        /*PegasosSVM pegasos = new PegasosSVM(numOfFeatures);
        pegasos.addTrainingProgressListener(new TrainingProgressPrinter());
        pegasos.setLambda(1.0E-4);
        pegasos.setNumIteration(500);
        pegasos.train(test);

        correct = 0;
        for (LabeledVector vector : test) {
            int predictedLabel = pegasos.classify(vector.getInnerVector());
            if (predictedLabel == vector.getLabel()) {
                correct++;
            }
        }

        System.out.println("A(PegasosSVM) = " + ((double) correct / test.size()));*/
  }

}
