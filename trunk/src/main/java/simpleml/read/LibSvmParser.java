package simpleml.read;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import simpleml.struct.IndexedValue;
import simpleml.struct.LabeledVector;
import simpleml.utils.VectorUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mtkachenko
 */
public class LibSvmParser extends Parser {

  private int numInstances;
  private int numFeatures;
  private int numClasses;

  @Override
  public void init() throws IOException {
    final TIntSet labelSet = new TIntHashSet();
    numFeatures = -1;
    numInstances = 0;
    try (final BufferedReader reader = new BufferedReader(new InputStreamReader(getSource().openStream(), "UTF-8"))) {
      String line;
      while (null != (line = reader.readLine())) {
        line = line.trim();
        final String[] row = line.split("[\\s:]");
        for (int i = 1; i < row.length; i += 2) {
          numFeatures = StrictMath.max(Integer.parseInt(row[i]) + 1, numFeatures);
        }
        labelSet.add(Integer.valueOf(trimPlus(row[0])));
        numInstances++;
      }
    }
    numClasses = labelSet.size();
  }

  @Override
  public int numClasses() {
    checkInitialization();
    return numClasses;
  }

  @Override
  public int numFeatures() {
    checkInitialization();
    return numFeatures;
  }

  public int numInstances() {
    return numInstances;
  }

  private String trimPlus(String s) {
    return s.length() > 0 && s.charAt(0) == '+' ? s.substring(1) : s;
  }

  @Override
  public List<LabeledVector> readAll() throws IOException {
    checkInitialization();
    final ArrayList<LabeledVector> result = new ArrayList<>(numInstances);
    try (final BufferedReader reader = new BufferedReader(new InputStreamReader(getSource().openStream(), "UTF-8"))) {
      String line;
      while (null != (line = reader.readLine())) {
        line = line.trim();
        final String[] vals = line.split("[\\s:]");
        int label = Integer.valueOf(trimPlus(vals[0]));
        final List<IndexedValue> values = new LinkedList<>();
        for (int i = 1; i < vals.length; i += 2) {
          values.add(new IndexedValue(Integer.parseInt(vals[i]), Double.parseDouble(vals[i + 1])));
        }
        result.add(new LabeledVector(VectorUtils.newSparseVector(values, numFeatures), label));
      }
    }

    return result;
  }

}
