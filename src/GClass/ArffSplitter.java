/**
 * <p>Title: ArffSplitter.java</p>
 *
 * <p>Description: Splits an arff file into a given number of parts.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


package GClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.*;

/**
 * Splits an arff file into a given number of parts.
 *
 * ------------------------------------------------------------------- <p>
 *
 * Valid options from the command line are:<p>
 *
 * -n number of splits <br>
 * The number of splits. <p>
 *
 * -i input arff file <br>
 * The input file in arff format. <p>
 *
 */

public class ArffSplitter implements BatchConverter, IncrementalConverter, Serializable {

    /** The write modes. */
    protected static final int WRITE = 0;
    protected static final int WAIT = 1;
    protected static final int CANCEL = 2;
    protected static final int STRUCTURE_READY = 3;

    /** The instances that should be stored. */
    private Instances m_instances;

    /** The current write mode. */
    private int m_writeMode;

    /** The destination files. */
    private File m_outputFile[];

    /** The writer. */
    private BufferedWriter m_writer;

    /** Counter. In incremental mode after reading 100 instances they will be written to a file.*/
    protected int m_incrementalCounter;

    /** The number of splits.*/
    private int No = 0;

    /** The arff input file.*/
    private String Filename = null;

    /** To check if the optrions are set.*/
    private boolean optionsSet = false;

    /** Constructor */
    public ArffSplitter(String[] options) {

        try {
            setOptions(options);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /** Constructor */
    public ArffSplitter(int n, String filename) {
        No = n;
        Filename = filename;
        optionsSet = true;
    }

    /**
     * Resets the Saver
     */
    public void reset() {

        m_instances = null;
        m_writeMode = WAIT;
        m_outputFile = null;
        m_writer = null;
        m_incrementalCounter = 0;
    }

    /** Resets the structure (header information of the instances). */
    public void resetStructure() {

        m_instances = null;
        m_writeMode = WAIT;
    }

    /**
     * Gets the No number of splits.
     *
     * @return the No number of splits
     */
    public int getNo() {

        return No;
    }

    /**
     * Gets the arff input filename.
     *
     * @return the arff input filename
     */
    public String getFilename() {

        return Filename;
    }

    /**
     * Gets a single output file from the array.
     *
     * @return the output file
     * @param i the index of the output file wanted
     */
    public File getOutputFile(int i) {

        return m_outputFile[i];
    }

    /**
     * Sets the write mode.
     *
     * @param mode the write mode
     */
    protected void setWriteMode(int mode) {

        m_writeMode = mode;
    }

    /**
     * Gets the write mode.
     *
     * @return the write mode
     */
    public int getWriteMode() {

        return m_writeMode;
    }

    /**
     * Sets instances that should be stored.
     *
     * @param instances the instances
     */
    public void setInstances(Instances instances) {

        m_instances = instances;
    }

    /**
     * Gets instances that should be stored.
     *
     * @return the instances
     */
    public Instances getInstances() {

        return m_instances;
    }

    /**
     * Sets the structure of the instances for the first step of incremental saving.
     * The instances only need to have a header.
     *
     * @param headerInfo an instances object.
     * @return the appropriate write mode
     */
    public int setStructure(Instances headerInfo) {

        if (m_writeMode == WAIT && headerInfo != null) {
            m_instances = headerInfo;
            m_writeMode = STRUCTURE_READY;
        } else {
            if ( (headerInfo == null) || ! (m_writeMode == STRUCTURE_READY) ||
                !headerInfo.equalHeaders(m_instances)) {
                m_instances = null;
                if (m_writeMode != WAIT) {
                    System.err.println(
                        "A structure cannot be set up during an active incremental saving process.");
                }
                m_writeMode = CANCEL;
            }
        }
        return m_writeMode;
    }

    /** Cancels the incremental saving process if the write mode is CANCEL. */
    public void cancel(File outputFile) {

        if (m_writeMode == CANCEL) {
            if (outputFile != null && outputFile.exists()) {
                if (outputFile.delete()) {
                    System.out.println("File deleted.");
                }
            }
            reset();
        }
    }

    /**
     * Gets the writer
     *
     * @return the BufferedWriter
     */
    public BufferedWriter getWriter() {

        return m_writer;
    }

    /**
     * Make up the help string giving all the command line options
     *
     * @return a string detailing the valid command line options
     */
    protected static String makeOptionString() {

        StringBuffer optionsText = new StringBuffer("");

        optionsText.append("\n\nArffSplitter usage:\n\n");
        optionsText.append("\n\nArffSplitter(<the number of splits>, <the input arff file>)\n\n");
        optionsText.append("\n\nor\n\n");
        optionsText.append("\n\nArffSplitter -n <the number of splits> -i <the input arff file>\n\n");

        return optionsText.toString();
    }

    /**
     * Parses a given list of options. Valid options are:<p>
     *
     * -n number of splits <br>
     * The number of splits. <p>
     *
     * -i input arff file <br>
     * The input file in arff format. <p>
     *
     * @param options the list of options as an array of strings
     * @exception Exception if an option is not supported
     */
    public void setOptions(String[] options) throws Exception {

        boolean optionSet = false;

        try {

            // Get No number of splits
            String NoString = Utils.getOption('n', options);
            if (NoString.length() != 0) {
                No = Integer.parseInt(NoString);
                optionSet = true;
            } else {
                throw new Exception(
                    "Number of splits needed via the -n option.");
            }

            // Get arff filename
            Filename = Utils.getOption('i', options);
            if (Filename.length() != 0) {

            } else {

                throw new IOException(
                    "Input file needed via the -i option.");
            }

        } catch (Exception e) {
            throw new Exception("\n" + e.getMessage() + makeOptionString());
        }

        if (optionSet && (Filename != null)) {
            optionsSet = true;
        }
    }

    /**
     * Initializes splitter with the given options.<p>
     *
     * @exception Exception if an option is not supported
     */
    public boolean initialize() throws Exception {

        ArffLoader loader = new ArffLoader();
        String FilenameExt = null;
        reset();

        if (optionsSet) {

            if (No != 0) {
                m_outputFile = new File[No];

                // create Filename and FilenameExt = Filename + Extension
                if (!Filename.endsWith(".arff")) {
                    if (Filename.contains(".")) {
                        throw new IOException(
                            "Wrong file type. Data set has to be in ARFF format." +
                            " (And filename and path should not contain any dots.)");
                    } else {
                        FilenameExt = Filename + ".arff";
                    }
                } else {
                    FilenameExt = Filename;
                    Filename = Filename.substring(0, Filename.lastIndexOf('.'));
                }

                // load input file
                try {
                    File input = new File(FilenameExt);
                    loader.setFile(input);
                    m_instances = loader.getDataSet();
                } catch (Exception ex) {
                    throw new IOException(
                        "No data set loaded. Data set has to be in ARFF format.");
                }

                // create output files
                for (int i = 0; i < No; i++) {
                    try {
                        m_outputFile[i] = new File(Filename + "_" + (i + 1) + "_" + No + ".arff");
                    } catch (Exception ex) {
                        throw new IOException(
                            "Cannot create output file.");
                    }
                }

            } else {
                throw new Exception(
                    "Meaningless operation. Number of splits is set to 0.");
            }
        } else {
            throw new Exception(
                "Options Error.");
        }

        if ( (FilenameExt != null) && (m_instances != null) && (m_outputFile != null)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the destination file (and directories if necessary).
     *
     * @param file the File
     * @exception IOException always
     */
    public void setDestination(File file) throws IOException {

        boolean success = false;
        String out = file.getAbsolutePath();
        if (file != null) {
            try {
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println(
                            "File exists and will be overridden.");
                    } else {
                        throw new IOException("File already exists.");
                    }
                }
                if (out.lastIndexOf(File.separatorChar) == -1) {
                    success = file.createNewFile();
                } else {
                    String outPath = out.substring(0,
                        out.lastIndexOf(File.separatorChar));
                    File dir = new File(outPath);
                    if (dir.exists()) {
                        success = file.createNewFile();
                    } else {
                        dir.mkdirs();
                        success = file.createNewFile();
                    }
                }
                if (success) {
                    m_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                }
            } catch (Exception ex) {
                throw new IOException(
                    "Cannot create a new output file. Standard out is used.");
            }
            finally {
                if (!success) {
                    System.err.println(
                        "Cannot create a new output file. Standard out is used.");
                    m_outputFile = null; //use standard out
                }
            }
        }
    }

    /**
     * Saves an instances incrementally. Structure has to be set by using the
     * setStructure() method or setInstances() method.
     *
     * @param inst the instance to save
     * @param outputFile the output File in which to save it
     * @throws IOException throws IOEXception if an instance cannot be saved incrementally.
     */
    public void writeIncremental(Instance inst, File outputFile) throws IOException {

        Instances structure = m_instances;
        PrintWriter outW = null;

        if (m_writer != null) {
            outW = new PrintWriter(m_writer);
        }

        if (m_writeMode == WAIT) {
            if (structure == null) {
                m_writeMode = CANCEL;
                if (inst != null) {
                    System.err.println(
                        "Structure(Header Information) has to be set in advance.");
                }
            } else {
                m_writeMode = STRUCTURE_READY;
            }
        }
        if (m_writeMode == CANCEL) {
            if (outW != null) {
                outW.close();
            }
            cancel(outputFile);
        }
        if (m_writeMode == STRUCTURE_READY) {
            m_writeMode = WRITE;
            //write header
            Instances header = new Instances(structure, 0);
            if (outputFile == null || outW == null) {
                System.out.println(header.toString());
            } else {
                outW.print(header.toString());
                outW.print("\n");
                outW.flush();
            }
        }
        if (m_writeMode == WRITE) {
            if (structure == null) {
                throw new IOException("No instances information available.");
            }
            if (inst != null) {
                //write instance
                if (outputFile == null || outW == null) {
                    System.out.println(inst);
                } else {
                    outW.println(inst);
                    m_incrementalCounter++;
                    //flush every 100 instances
                    if (m_incrementalCounter > 100) {
                        m_incrementalCounter = 0;
                        outW.flush();
                    }
                }
            } else {
                //close
                if (outW != null) {
                    outW.flush();
                    outW.close();
                }
                m_incrementalCounter = 0;
                resetStructure();
            }
        }
    }

    public void split() {

        try {
            if (initialize()) {
                Instances instances = m_instances;

                // Stratifies the instances according to their class values if the class attribute is nominal
                instances.setClassIndex(instances.numAttributes() - 1);
                if (instances.classAttribute().isNominal()) {
                    int i = 1;
                    while (i < instances.numInstances()) {
                        Instance instance1 = instances.instance(i - 1);
                        for (int j = i; j < instances.numInstances(); j++) {
                            Instance instance2 = instances.instance(j);
                            if (instance1.classValue() == instance2.classValue()) {
                                instances.swap(i, j);
                                i++;
                            }
                        }
                        i++;
                    }
                }

                for (int i = 0; i < No; i++) {
                    setStructure(instances);
                    setDestination(m_outputFile[i]);
                    // Saves every i+n instance in the i file (n the number of files)
                    for (int j = i; j < instances.numInstances(); j += No) { //last instance is null and finishes incremental saving
                        writeIncremental(instances.instance(j), m_outputFile[i]);
                    }
                    writeIncremental(null, m_outputFile[i]);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Main method.
     *
     * @param options should contain the following options:
     * -n number of splits
     * -i input arff file
     */
    public static void main(String[] options) {

        long startTime = System.currentTimeMillis();

        ArffSplitter splitter = new ArffSplitter(options);
        splitter.split();

        System.out.println("ArffSplitter" + Total.showTime(System.currentTimeMillis() - startTime));

    }
}
