package org.simpleml.classify.notify.progress;

/**
 * '.' - instance
 * '*' - iteration
 * '!' - finished
 *
 * @author sitfoxfly
 */
public class TrainingProgressPrinter implements TrainingProgressListener {

    private static final long DEFAULT_INSTANCE_CUTOFF = 1000;
    private static final long DEFAULT_ITERATION_CUTOFF = 1;

    private final long printEachInstance;
    private final long printEachIteration;

    private long instanceCounter = 0;
    private long iterationCounter = 0;

    public TrainingProgressPrinter(long printEachInstance, long printEachIteration) {
        this.printEachInstance = printEachInstance;
        this.printEachIteration = printEachIteration;
    }

    public TrainingProgressPrinter(int printEachInstance) {
        this(printEachInstance, DEFAULT_ITERATION_CUTOFF);
    }

    public TrainingProgressPrinter() {
        this(DEFAULT_INSTANCE_CUTOFF, DEFAULT_ITERATION_CUTOFF);
    }

    @Override
    public void progressUpdate(TrainingProgressEvent event) {
        switch (event.getType()) {
            case START_TRAINING:
                instanceCounter = 0;
                iterationCounter = 0;
                break;
            case FINISH_INSTANCE_PROCESSING:
                instanceCounter++;
                if (instanceCounter % printEachInstance == 0) {
                    System.out.print(".");
                    instanceCounter = 0;
                }
                break;
            case FINISH_ITERATION:
                iterationCounter++;
                if (iterationCounter % printEachIteration == 0) {
                    System.out.print("*");
                    iterationCounter = 0;
                }
                break;
            case FINISH_TRAINING:
                System.out.println("!");
                break;
        }
    }
}
