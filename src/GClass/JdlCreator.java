
/**
 * <p>Title: JdlCreator.java</p>
 *
 * <p>Description: Creates a jdl and an sh file.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


package GClass;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Class for creating the jdl and sh files. <p>
 *
 * ------------------------------------------------------------------- <p>
 *
 */

public class JdlCreator {

    /** The base parameters for the jdl and sh files. */
    private String baseFileName;
    private String fileURL;
    private int numberOfSplits;

    private int parametersSet = 0;

    /** Creates the jdl file for the ArffSplitter. */
    public void createArffSplitterJDL() {
        if (parametersSet == 0) {
            System.out.println("Parameters for JDL / SH creation not set...");
            return;
        }

        String type = "Type = \"job\";";

        String jobType = "JobType = \"normal\";";

        String virtualOrganisation = "VirtualOrganisation = \"see\";";

        String executable = "Executable = \"arffSplitter.sh\";";

        String arguments = "Arguments = \"-cp .:weka.jar:mssqlserver.jar:msutil.jar:msbase.jar -Xmx512M";
        arguments += " GClass.ArffSplitter -n 5 -i " + baseFileName + ".arff\";";

        String stdOutput = "StdOutput = \"arffSplitter.out\";";

        String stdError = "StdError = \"arffSplitter.err\";";

        String inputSandBox = "InputSandBox = {";
        inputSandBox += "\"arffSplitter.sh\"";
        inputSandBox += ", \"classes/ArffSplitter.class\"";
        inputSandBox += ", \"lib/weka.jar\", \"lib/mssqlserver.jar\", \"lib/msutil.jar\", \"lib/msbase.jar\"";
        inputSandBox += ", \"lib/database.properties\"";
        inputSandBox += "};";

        String outputSandBox = "OutputSandBox = {";
        outputSandBox += "\"arffSplitter.out\", \"arffSplitter.err\", ";
        for (int i = 1; i <= numberOfSplits; i++) {
            if (i == numberOfSplits) {
                outputSandBox += "\"" + baseFileName + "_" + i + "_" + numberOfSplits + ".arff\"";
            } else {
                outputSandBox += "\"" + baseFileName + "_" + i + "_" + numberOfSplits + ".arff\", ";
            }
        }
        outputSandBox += "};";

        String retryCount = "RetryCount = 5;";

        String myProxyServer = "MyProxyServer = \"myproxy.grid.auth.gr\";";

        String requirements = "Requirements = (other.GlueHostNetworkAdapterOutboundIP==\"TRUE\") && ";
        requirements += " (other.GlueCEPolicyMaxCPUTime>259200);"; //259200 minutes == 48 hours

        try {
            FileOutputStream fout = new FileOutputStream("arffSplitter.jdl");
            PrintStream myOutput = new PrintStream(fout);
            myOutput.println(type);
            myOutput.println(jobType);
            myOutput.println(virtualOrganisation);
            myOutput.println(executable);
            myOutput.println(arguments);
            myOutput.println(stdOutput);
            myOutput.println(stdError);
            myOutput.println(inputSandBox);
            myOutput.println(outputSandBox);
            myOutput.println(retryCount);
            myOutput.println(myProxyServer);
            myOutput.println(requirements);
        }
        catch (IOException exp) {
            System.out.println("Error: " + exp);
            exp.printStackTrace();
        }
    }

    /** Creates the sh file for the ArffSplitter. */
    public void createArffSplitterSH() {
        if (parametersSet == 0) {
            System.out.println("Parameters for JDL / SH creation not set...");
            return;
        }

        try {
            FileOutputStream fout = new FileOutputStream("arffSplitter.sh");
            PrintStream myOutput = new PrintStream(fout);
            myOutput.println("#!/bin/sh");
            myOutput.println("/bin/date");
            myOutput.println("/bin/hostname -f");
            myOutput.println("WGET_FILE=`/usr/bin/which wget`");
            myOutput.println("$WGET_FILE " + fileURL + baseFileName + ".arff");
            myOutput.println("JAVA_BIN=`/usr/bin/which java`");
            myOutput.println("echo Found java at $JAVA_BIN");
            myOutput.println("$JAVA_BIN $*");
            myOutput.println("/bin/date");
        }
        catch (IOException exp) {
            System.out.println("Error: " + exp);
            exp.printStackTrace();
        }
    }

    /** Creates the jdl file for the J48 training of the individual models. */
    public void createTrainingJDL() {
        if (parametersSet == 0) {
            System.out.println("Parameters for JDL / SH creation not set...");
            return;
        }

        for (int i = 1; i <= numberOfSplits; i++) {
            String type = "Type = \"job\";";

            String jobType = "JobType = \"normal\";";

            String virtualOrganisation = "VirtualOrganisation = \"see\";";

            String executable = "Executable = \"training" + i + ".sh\";";

            String arguments = "Arguments = \"-cp .:weka.jar:mssqlserver.jar:msutil.jar:msbase.jar -Xmx512M";
            arguments += " weka.classifiers.trees.J48 -t " + baseFileName + "_" + i + "_" + numberOfSplits + ".arff";
            arguments += " -d " + baseFileName + "_" + i + "_" + numberOfSplits + ".model -x 10";
            //arguments += " -T " + baseFileName + ".arff";
            arguments += "\";";

            String stdOutput = "StdOutput = \"training" + i + ".out\";";

            String stdError = "StdError = \"training" + i + ".err\";";

            String inputSandBox = "InputSandBox = {";
            inputSandBox += "\"training" + i + ".sh\"";
            inputSandBox += ", \"lib/weka.jar\", \"lib/mssqlserver.jar\", \"lib/msutil.jar\", \"lib/msbase.jar\"";
            inputSandBox += ", \"lib/database.properties\"";
            inputSandBox += "};";

            String outputSandBox = "OutputSandBox = {";
            outputSandBox += "\"training" + i + ".out\", \"training" + i + ".err\", \"" + baseFileName + "_" + i + "_" + numberOfSplits + ".model\"";
            outputSandBox += "};";

            String retryCount = "RetryCount = 5;";

            String myProxyServer = "MyProxyServer = \"myproxy.grid.auth.gr\";";

            String requirements = "Requirements = (other.GlueHostNetworkAdapterOutboundIP==\"TRUE\") && ";
            requirements += " (other.GlueCEPolicyMaxCPUTime>259200);"; //259200 minutes == 48 hours

            try {
                FileOutputStream fout = new FileOutputStream("training" + i + ".jdl");
                PrintStream myOutput = new PrintStream(fout);
                myOutput.println(type);
                myOutput.println(jobType);
                myOutput.println(virtualOrganisation);
                myOutput.println(executable);
                myOutput.println(arguments);
                myOutput.println(stdOutput);
                myOutput.println(stdError);
                myOutput.println(inputSandBox);
                myOutput.println(outputSandBox);
                myOutput.println(retryCount);
                myOutput.println(myProxyServer);
                myOutput.println(requirements);
            }
            catch (IOException exp) {
                System.out.println("Error: " + exp);
                exp.printStackTrace();
            }
        }
    }

    /** Creates the sh file for the J48 training of the individual models. */
    public void createTrainingSH() {
        if (parametersSet == 0) {
            System.out.println("Parameters for JDL / SH creation not set...");
            return;
        }

        for (int i = 1; i <= numberOfSplits; i++) {
            try {
                FileOutputStream fout = new FileOutputStream("training" + i + ".sh");
                PrintStream myOutput = new PrintStream(fout);
                myOutput.println("#!/bin/sh");
                myOutput.println("/bin/date");
                myOutput.println("/bin/hostname -f");
                myOutput.println("WGET_FILE=`/usr/bin/which wget`");
                //myOutput.println("$WGET_FILE " + fileURL + baseFileName + ".arff");
                myOutput.println("$WGET_FILE " + fileURL + baseFileName + "_" + i + "_" + numberOfSplits + ".arff");
                myOutput.println("JAVA_BIN=`/usr/bin/which java`");
                myOutput.println("echo Found java at $JAVA_BIN");
                myOutput.println("$JAVA_BIN $*");
                myOutput.println("/bin/date");
            }
            catch (IOException exp) {
                System.out.println("Error: " + exp);
                exp.printStackTrace();
            }
        }
    }

    /** Creates the jdl file for the ClassifiersCombiner. */
    public void createCombinerJDL() {

        if (parametersSet == 0) {
            System.out.println("Parameters for JDL / SH creation not set...");
            return;
        }

        String type = "Type = \"job\";";

        String jobType = "JobType = \"normal\";";

        String virtualOrganisation = "VirtualOrganisation = \"see\";";

        String executable = "Executable = \"combiner.sh\";";

        String arguments = "Arguments = \"-cp .:weka.jar:mssqlserver.jar:msutil.jar:msbase.jar -Xmx512M GClass.ClassifierCombiner";
        arguments += " -T " + baseFileName + ".arff";
        for (int i = 1; i <= numberOfSplits; i++) {
            arguments += " -L " + baseFileName + "_" + i + "_" + numberOfSplits + ".model";
        }
        arguments += "\";";

        String stdOutput = "StdOutput = \"combiner.out\";";

        String stdError = "StdError = \"combiner.err\";";

        String inputSandBox = "InputSandBox = {";
        inputSandBox += "\"combiner.sh\"";
        inputSandBox += ", \"classes/ClassifiersCombiner.class\"";
        inputSandBox += ", \"lib/weka.jar\", \"lib/mssqlserver.jar\", \"lib/msutil.jar\", \"lib/msbase.jar\"";
        inputSandBox += ", \"lib/database.properties\"";
        inputSandBox += "};";

        String outputSandBox = "OutputSandBox = {";
        outputSandBox += "\"combiner.out\", \"combiner.err\"};";

        String retryCount = "RetryCount = 5;";

        String myProxyServer = "MyProxyServer = \"myproxy.grid.auth.gr\";";

        String requirements = "Requirements = (other.GlueHostNetworkAdapterOutboundIP==\"TRUE\") && ";
        requirements += " (other.GlueCEPolicyMaxCPUTime>259200);"; //259200 minutes == 48 hours

        try {
            FileOutputStream fout = new FileOutputStream("combiner.jdl");
            PrintStream myOutput = new PrintStream(fout);
            myOutput.println(type);
            myOutput.println(jobType);
            myOutput.println(virtualOrganisation);
            myOutput.println(executable);
            myOutput.println(arguments);
            myOutput.println(stdOutput);
            myOutput.println(stdError);
            myOutput.println(inputSandBox);
            myOutput.println(outputSandBox);
            myOutput.println(retryCount);
            myOutput.println(myProxyServer);
            myOutput.println(requirements);
        }
        catch (IOException exp) {
            System.out.println("Error: " + exp);
            exp.printStackTrace();
        }
    }

    /** Creates the sh file for the ClassifiersCombiner. */
    public void createCombinerSH() {
        if (parametersSet == 0) {
            System.out.println("Parameters for JDL / SH creation not set...");
            return;
        }
        try {
            FileOutputStream fout = new FileOutputStream("combiner.sh");
            PrintStream myOutput = new PrintStream(fout);
            myOutput.println("#!/bin/sh");
            myOutput.println("/bin/date");
            myOutput.println("/bin/hostname -f");
            myOutput.println("WGET_FILE=`/usr/bin/which wget`");
            myOutput.println("$WGET_FILE " + fileURL + baseFileName + ".arff");
            for (int i = 1; i <= numberOfSplits; i++) {
                myOutput.println("$WGET_FILE " + fileURL + baseFileName + "_" + i + "_" + numberOfSplits + ".model");
            }
            myOutput.println("JAVA_BIN=`/usr/bin/which java`");
            myOutput.println("echo Found java at $JAVA_BIN");
            myOutput.println("$JAVA_BIN $*");
            myOutput.println("/bin/date");
        }
        catch (IOException exp) {
            System.out.println("Error: " + exp);
            exp.printStackTrace();
        }
    }

    /**
     * Sets the base parameters for the jdl and sh files.
     *
     * @param baseFileName_ext the base file name
     * @param fileURL_ext the URL of the file
     * @param numOfClasses the number of classes
     * @param numOfSplits the number of splits
     */
    public void setJDL_SHParameters(String baseFileName_ext, String fileURL_ext, int numOfSplits) {
        baseFileName = baseFileName_ext;
        fileURL = fileURL_ext;
        numberOfSplits = numOfSplits;
        parametersSet = 1;

    }

    /** Sets the base parameters' default values. */
    private void setDefaultJDL_SHParameters() {
        baseFileName = "genbase_larger10";
        fileURL = "http://olympus.ee.auth.gr/~fpsom/";
        numberOfSplits = 5;
    }

    /** Main method. */
    public static void main(String[] args) {
        JdlCreator jdl = new JdlCreator();

        jdl.setDefaultJDL_SHParameters();

        jdl.createArffSplitterJDL();
        jdl.createArffSplitterSH();

        jdl.createTrainingJDL();
        jdl.createTrainingSH();

        jdl.createCombinerJDL();
        jdl.createCombinerSH();
    }
}
