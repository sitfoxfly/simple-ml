package simpleml.main;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;
import simpleml.classify.Classifier;
import simpleml.classify.Trainable;
import simpleml.classify.ext.ExtUtils;
import simpleml.classify.ext.ExternalizableModel;
import simpleml.classify.notify.progress.TrainingProgressNotifier;
import simpleml.classify.notify.progress.TrainingProgressPrinter;
import simpleml.read.LibSvmParser;
import simpleml.read.Parser;
import simpleml.struct.LabeledVector;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author mtkachenko
 */
public class CLI {

  private static final String CLASSIFY = "classify";
  private static final String TRAIN = "train";

  public static abstract class Command {

    private String name;

    protected Command(String name) {
      this.name = name;
    }

    public String name() {
      return name;
    }

    @Option(name = "-t", metaVar = "classifier_type", usage = "" +
        "0 - linear perceptron (default)\n" +
        "1 - averaged linear perceptron\n" +
        "2 - passive-aggressive perceptron\n" +
        "3 - Pegasos SVM")
    protected int classifierType = 0;

    public abstract void exec() throws Exception;

  }

  public static class ClassifyCommand extends Command {

    public ClassifyCommand() {
      super(CLASSIFY);
    }

    @Argument(required = true, metaVar = "model_file", index = 0, usage = "trained model")
    private File modelFile;

    @Argument(required = true, metaVar = "test_file", index = 1, usage = "test file (URL is supported)")
    private String dataFile;

    @Argument(required = true, metaVar = "output_file", index = 2, usage = "prediction output")
    private String outFile;

    @Override
    public void exec() throws Exception {
      final Classifier classifier = (Classifier) ExtUtils.load(ClassifierFactory.INSTANCE.getModelClass(classifierType), modelFile);

      final Parser dataParser = new LibSvmParser();
      dataParser.setSource(dataFile);
      dataParser.initialize();

      try (final PrintWriter out = new PrintWriter(outFile)) {
        final List<LabeledVector> labeledVectors = dataParser.readAll();
        System.out.println("=== DATA SUMMARY ===");
        System.out.println("  # instances = " + labeledVectors.size());
        System.out.println("  # labels = " + dataParser.numClasses());
        System.out.println();

        int tp = 0, fp = 0, fn = 0, tn = 0;

        for (LabeledVector labeledVector : labeledVectors) {
          final int dataLabel = labeledVector.getLabel();
          final int label = classifier.classify(labeledVector);

          if (dataLabel == 1 && label == 1) {
            tp++;
          } else if (dataLabel == 1 && label == -1) {
            fn++;
          } else if (dataLabel == -1 && label == 1) {
            fp++;
          } else if (dataLabel == -1 && label == -1) {
            tn++;
          }

          out.println(label);
        }

        final double a = ((double) tp + tn) / (tp + tn + fn + fp);
        final double p = ((double) tp) / (tp + fp);
        final double r = ((double) tp) / (tp + fn);
        final double f1 = 2.0 * p * r / (p + r);

        System.out.println("=== EVALUATION ===");
        System.out.println("  Accuracy  = " + a);
        System.out.println("  Precision = " + p);
        System.out.println("  Recall    = " + r);
        System.out.println("  F1        = " + f1);
        System.out.println();
      }

      ExtUtils.save((ExternalizableModel) classifier, modelFile);
    }

  }

  public static class TrainCommand extends Command {

    public TrainCommand() {
      super(TRAIN);
    }

    @Argument(required = true, metaVar = "training_file", index = 0, usage = "training set file")
    private String dataFile;

    @Argument(required = true, metaVar = "model_file", index = 1, usage = "output model")
    private File modelFile;

    @Override
    public void exec() throws Exception {
      final Parser dataParser = new LibSvmParser();
      dataParser.setSource(dataFile);
      dataParser.initialize();

      final Trainable classifier;
      if (dataParser.numClasses() == 2) {
        classifier = ClassifierFactory.INSTANCE.build(classifierType, dataParser.numFeatures());
      } else if (dataParser.numClasses() > 2) {
        throw new IllegalArgumentException("multilabel training has not supported yet");
      } else {
        throw new IllegalArgumentException("the training file '" + dataFile + "' contains only one label");
      }
      if (classifier instanceof TrainingProgressNotifier) {
        ((TrainingProgressNotifier) classifier).addTrainingProgressListener(new TrainingProgressPrinter());
      }
      classifier.train(dataParser.readAll());

      ExtUtils.save((ExternalizableModel) classifier, modelFile);
    }

  }

  @Argument(handler = SubCommandHandler.class, required = true, metaVar = "command", usage = "train - train a classifier\nclassify - classify a test data")
  @SubCommands({
      @SubCommand(name = TRAIN, impl = CLI.TrainCommand.class),
      @SubCommand(name = CLASSIFY, impl = CLI.ClassifyCommand.class),
  })
  private Command command;

  private void run(String[] args) {
    final CmdLineParser parser = new CmdLineParser(this);
    try {
      parser.parseArgument(args);
      command.exec();
    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      System.out.print("USAGE: <CLI>");
      e.getParser().printSingleLineUsage(System.out);
      System.out.println();
      System.out.println("Options:");
      e.getParser().printUsage(System.out);
      System.exit(-1);
    } catch (Exception e) {
      System.err.println("EXCEPTION: " + e.getMessage());
      System.exit(-1);
    }
  }

  public static void main(String[] args) {
    new CLI().run(args);
  }

}
