package org.simpleml;

import org.simpleml.classify.AveragedLinearPerceptron;
import org.simpleml.classify.Classifier;
import org.simpleml.classify.OneInstPegasosSVM;
import org.simpleml.struct.LabeledVector;
import org.simpleml.struct.SparseHashVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sitfoxfly
 */
public class LibSvmDataset {

    private File file;
    private int numOfFeatures;

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
        List<LabeledVector> result = new LinkedList<LabeledVector>();
        BufferedReader reader = new BufferedReader(new FileReader(this.file));

        String line;
        while (null != (line = reader.readLine())) {
            line = line.trim();
            String[] vals = line.split("[\\s:]");
            int label = Integer.valueOf(cutPlus(vals[0]));
            List<IndexedValue> values = new LinkedList<IndexedValue>();
            for (int i = 1; i < vals.length; i += 2) {
                values.add(new IndexedValue(Integer.parseInt(vals[1]), Double.parseDouble(vals[2])));
            }
            result.add(new LabeledVector(new SparseHashVector(values, numOfFeatures), label));
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        final int numOfFeatures = 5000;
        LibSvmDataset dataset = new LibSvmDataset(new File("dataset/gisette_scale"), numOfFeatures);
        Classifier perceptron = new AveragedLinearPerceptron(numOfFeatures);

        final List<LabeledVector> list = dataset.readAll();
        perceptron.train(list);

        int correct;

        correct = 0;
        for (LabeledVector vector : list) {
            int predictedLabel = perceptron.classify(vector.getInnerVector());
            if (predictedLabel == vector.getLabel()) {
                correct++;
            }
        }

        System.out.println("A(AvgLinPer) = " + ((double) correct / list.size()));

        Classifier pegasos = new OneInstPegasosSVM(numOfFeatures);
        pegasos.train(list);

        correct = 0;
        for (LabeledVector vector : list) {
            int predictedLabel = pegasos.classify(vector.getInnerVector());
            if (predictedLabel == vector.getLabel()) {
                correct++;
            }
        }

        System.out.println("A(PegasosSVM) = " + ((double) correct / list.size()));
    }

}
