/**
 * <p>Title: ClassifiersCombiner.java</p>
 *
 * <p>Description: Class for combining classifiers using unweighted average of
 * probability estimates (classification).</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


package GClass;

import java.io.*;
import java.util.*;
import weka.core.*;
import weka.classifiers.*;
import weka.classifiers.Classifier;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.Option;
import weka.core.Instances;
import java.util.Vector;
import java.util.Enumeration;

/**
 * Class for combining classifiers using unweighted average of
 * probability estimates (classification).
 *
 * ------------------------------------------------------------------- <p>
 *
 * Valid options from the command line are:<p>
 *
 * -L filename <br>
 * Filename should contain the name of a file storing a classifier model.
 * (required, option should be used once for each classifier).<p>
 *
 */

public class ClassifierCombiner extends Classifier {

    /** Array for storing the base classifiers. */
    protected Classifier[] m_Classifiers;

    private String[] Filenames = null;

    /**
     * Returns a string describing classifier
     * @return a description suitable for
     * displaying in the explorer/experimenter gui
     */
    public String globalInfo() {

        return "Class for combining classifiers using unweighted average of "
            + "probability estimates (classification).";
    }

    /**
     * Returns an enumeration describing the available options
     *
     * @return an enumeration of all the available options
     */
    public Enumeration listOptions() {

        Vector newVector = new Vector(1);

        newVector.addElement(new Option(
            "\tFile name of classifier model to include.\n"
            + "\tMay be specified multiple times.\n"
            + "\t", "L", 1, "-L <classifier model file>"));

        Enumeration enu = super.listOptions();
        while (enu.hasMoreElements()) {
            newVector.addElement(enu.nextElement());
        }
        return newVector.elements();
    }

    public void getOptions(String[] options) throws Exception {
        Filenames = options;
        try {
            initialize();
        } catch (Exception e) {
            throw new Exception("\n" + e.getMessage());
        }

    }

    /**
     * Parses a given list of options. Valid options are:<p>
     *
     * -L filename <br>
     * Filename should contain the name of a file storing a classifier model.
     * (required, option should be used once for each classifier).<p>
     *
     * @param options the list of options as an array of strings
     * @exception Exception if an option is not supported
     */
    public void setOptions(String[] options) throws Exception {

        int N = 0;
        Vector FilenamesVector = new Vector();

        while (true) {
            // Get classifier model file names from options
            String objectInputFileName = Utils.getOption('L', options);
            if (objectInputFileName.length() == 0) {
                break;
            } else {
                FilenamesVector.addElement(objectInputFileName);
            }
            N++;
        }
        if (N == 0) {
            throw new Exception("Options Error: No classifier models given.");
        } else {
            try {
                Filenames = new String[N];
                for (int i = 0; i < N; i++) {
                    Filenames[i] = (String) FilenamesVector.elementAt(i);
                }
                initialize();
            } catch (Exception e) {
                throw new Exception("\n" + e.getMessage());
            }
        }
    }

    public void initialize() throws Exception {

        int N = Filenames.length;
        ObjectInputStream objectInputStream = null;
        m_Classifiers = new Classifier[N];

        for (int i = 0; i < N; i++) {
            // Get classifier model
            try {
                InputStream is = new FileInputStream(Filenames[i]);
                objectInputStream = new ObjectInputStream(is);
                m_Classifiers[i] = (Classifier) objectInputStream.readObject();
                objectInputStream.close();
            } catch (Exception e) {
                throw new Exception("Can't open file " + e.getMessage() + '.');
            }
        }
    }

    /**
     * Returns the tip text for this property
     * @return tip text for this property suitable for
     * displaying in the explorer/experimenter gui
     */
    public String classifiersTipText() {
        return "The base classifiers to be used.";
    }

    /**
     * Sets the list of possible classifiers to choose from.
     *
     * @param classifiers an array of classifiers with all options set.
     */
    public void setClassifiers(Classifier[] classifiers) {

        m_Classifiers = classifiers;
    }

    /**
     * Sets a single classifier in the set.
     *
     * @param index the index of the classifier
     * @param classifier a classifier
     */
    public void setClassifier(int index, Classifier classifier) {

        m_Classifiers[index] = classifier;
    }

    /**
     * Gets the list of possible classifers to choose from.
     *
     * @return the array of Classifiers
     */
    public Classifier[] getClassifiers() {

        return m_Classifiers;
    }

    /**
     * Gets a single classifier from the set of available classifiers.
     *
     * @param index the index of the classifier wanted
     * @return the Classifier
     */
    public Classifier getClassifier(int index) {

        return m_Classifiers[index];
    }

    /**
     * Gets the classifier specification string, which contains the class name of
     * the classifier and any options to the classifier
     *
     * @param index the index of the classifier string to retrieve, starting from 0.
     * @return the classifier string, or the empty string if no classifier
     * has been assigned (or the index given is out of range).
     */
    protected String getClassifierSpec(int index) {

        if (m_Classifiers.length < index) {
            return "";
        }
        Classifier c = m_Classifiers[index];
        return c.getClass().getName() + " " + Utils.joinOptions( ( (OptionHandler) c).getOptions());
    }

    /**
     * Empty method, set just to override abstract method from parent class
     *
     * @param data
     */
    public void buildClassifier(Instances data) {
    }

    /**
     * Classifies a given instance using the selected classifier.
     *
     * @param instance the instance to be classified
     * @exception Exception if instance could not be classified
     * successfully
     */
    public double[] distributionForInstance(Instance instance) throws Exception {

        double[] probs = m_Classifiers[0].distributionForInstance(instance);
        for (int i = 1; i < m_Classifiers.length; i++) {
            double[] dist = m_Classifiers[i].distributionForInstance(instance);
            for (int j = 0; j < dist.length; j++) {
                probs[j] += dist[j];
            }
        }
        for (int j = 0; j < probs.length; j++) {
            probs[j] /= (double) m_Classifiers.length;
        }
        return probs;
    }

    /**
     * Output a representation of ClassifiersCombiner
     */
    public String toString() {

        if (m_Classifiers == null) {
            return "ClassifiersCombiner: No base classifiers acquired yet .";
        }

        String result = "ClassifiersCombiner combines";
        result += " the probability distributions of these base classifiers:\n";
        for (int i = 0; i < m_Classifiers.length; i++) {
            result += '\t' + getClassifierSpec(i) + '\n';
        }

        return result;
    }

    /**
     * Main method.
     *
     * @param argv should contain the following arguments:
     * -T test file
     * -L classifier model file (multiple times)
     */
    public static void main(String[] argv) {

        try {
            long startTime = System.currentTimeMillis();
            System.out.println(CombinerEvaluation.evaluateModel(new ClassifierCombiner(), argv));

            System.out.println("ClassifiersCombiner" + Total.showTime(System.currentTimeMillis() - startTime));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
