# Simple-ML #

The library can be used to attack large-scale classification problems. It is really fast!

Simple-ML supports:
  * Pegasos SVM
  * Linear Perceptron
  * Passive-Agressive Perceptron
  * Averaged Perceptron

## Download ##

  * [simple-ml-0.1-with-deps.jar](https://drive.google.com/uc?export=download&id=0B763_UsFTFjvNl93akYwRXNRekU)
  * [simple-ml-0.1.jar](https://drive.google.com/uc?export=download&id=0B763_UsFTFjvYk1DbjF3X05yZG8)

## Usage CLI (_Experimental_) ##

Simple-ML similar in usage to [LibSVM](http://www.csie.ntu.edu.tw/~cjlin/libsvm) and [SVM-Light](http://svmlight.joachims.org). The library is written in Java what requires JRE 7 installed on your OS. The convinient way to use `simple-ml-*.*-with-deps.jar` distribution, which requires minimum efforts in installation.

Simple-ML consists of a training module and a classification module. Classification module is used to apply the learned model to test examples.

The training module is called with the following parameters:

```
java -jar "simple-ml-0.1-with-deps.jar" train [options] <training_set_file> <model_file>
```

The classification module is called with the following parameters:

```
java -jar "simple-ml-0.1-with-deps.jar" classify [options] <model_file> <test_file> <output_file>
```

The only available option now is:
```
-t classifier_type : 0 - linear perceptron (default)
                     1 - averaged linear perceptron
                     2 - passive-aggressive perceptron
                     3 - Pegasos SVM
```

## Data Formats ##

### LibSVM Input Data Format ###

Simple-ML compatible with LibSVM data format:

```
<label> <index1>:<value1> <index2>:<value2> ... 
.
.
.
```

Each line contains one instance.  For classification, `<label>` is an integer indicating the class label (CLI supports only binary classification what restricts `<label>` to be `-1` or `+1`, but multilabel classification is supported in API). The pair `<index>:<value>` gives a feature value: `<index>` is an integer starting from 1 and `<value>` is a real number.

### Output Format ###

Each line of output file contains `<label>` for the corresponding instance of the test file.

## Terms of Use ##

If you are keen to use Simple-ML for non-commertial/research projects, please spread the project link https://code.google.com/p/simple-ml/ wherever you want and can. Also, drop me an e-mail if you want to use Simple-ML for commertial purposes.

# Cheers #

Now you know everything to be able to use Simple-ML in your data minig tasks. The library is still under our lazy development, so if you find a bug or you have a suggestion we would be glad to know. Please use the [issue tracker](https://github.com/sitfoxfly/simple-ml/issues) or e-mail. If you want to become a contributor, fell free to drop me an e-mail.

Since the training and classification algorithms implemented in Simple-ML are so fast, you will definitely have more time for ~~beer~~ your research!
