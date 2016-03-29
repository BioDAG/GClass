/**
 * <p>Title: Total.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


package GClass;

import weka.classifiers.trees.J48;

public class Total {

    private int No = 0;
    private String Filename = null;
    private String[] modelsString = null;

    public static String showTime(long time) {

        String string = null;
        float Time = time / 1000;
        if (Time < 60) {
            string = " took: \t" + Time + " seconds.";
        } else if (Time < 3600) {
            string = " took: \t" + Time / 60 + " minutes.";
        } else {
            string = " took: \t" + Time / 3600 + " hours.";
        }
        return string;
    }

    public static void main(String[] options) {

        Total total = new Total();
        ArffSplitter splitter = new ArffSplitter(options);
        total.No = splitter.getNo();
        total.Filename = splitter.getFilename();
        total.Filename = total.Filename.substring(0, total.Filename.lastIndexOf('.'));
        total.modelsString = new String[total.No + 1];
        total.modelsString[0] = total.Filename + ".arff";
        String output[] = null;

        String Result = "\nResults for " + total.Filename + " and " + total.No + " splits.\n";
        long startTime = System.currentTimeMillis();
        long Time = 0;

        // ArffSplitter
        Time = System.currentTimeMillis();
        splitter.split();
        Result += "\nArffSplitter       ";
        Result += Total.showTime(System.currentTimeMillis() - Time);
        Result += "\n";

        // J48 classifiers' training
        String trainFileName = null;
        String objectOutputFileName = null;
        int i1 = 0;
        for (int i = 0; i < total.No; i++) {
            try {
                i1 = i + 1;
                trainFileName = total.Filename + "_" + i1 + "_" + total.No + ".arff";
                objectOutputFileName = total.Filename + "_" + i1 + "_" + total.No + ".model";
                total.modelsString[i1] = objectOutputFileName;
                Time = System.currentTimeMillis();
                output = EvaluationInternal.evaluateModel(new J48(), trainFileName, objectOutputFileName);
                System.out.println(output[0]);
                Result += "\nClassifier " + i1 + " training";
                Result += Total.showTime(System.currentTimeMillis() - Time);
                Result += output[1];
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        // ClassifiersCombiner
        try {
            Time = System.currentTimeMillis();
            output = CombinerEvaluation.evaluateModelInternal(new ClassifierCombiner(), total.modelsString);
            System.out.println(output[0]);
            Result += "\n\nClassifiersCombiner";
            Result += Total.showTime(System.currentTimeMillis() - Time);
            Result += output[1];
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Total
        Result += "\n\nTotal procedure    ";
        Result += Total.showTime(System.currentTimeMillis() - startTime);
        System.out.println(Result);

    }
}
