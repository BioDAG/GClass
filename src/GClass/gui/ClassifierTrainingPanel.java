/**
 *
 */

package GClass.gui;

import GClass.EvaluationInternal;
import GClass.JdlCreator;
import GClass.Total;
import weka.classifiers.trees.J48;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;

public class ClassifierTrainingPanel extends JPanel implements SwingConstants, ItemListener {

    protected JTextArea outputText = new JTextArea();
    protected JButton startButton = new JButton("Start");
    protected JButton jdlButton = new JButton("Create JDL and SH file");
    protected JCheckBox previousCheckBox = new JCheckBox("Use the arff files created in the previous panel");
    protected JFileChooser newFileChooser = new JFileChooser();
    protected String filename = null;
    protected String pathfilename = null;
    protected String[] pathfilenames = null;
    protected int N = 0;

    /**
     * Creates the panel
     */
    public ClassifierTrainingPanel() {

        try {
            // options panel
            previousCheckBox.addItemListener(this);

            JPanel previousPanel = new JPanel();
            previousPanel.setLayout(new BoxLayout(previousPanel, BoxLayout.PAGE_AXIS));
            previousPanel.setAlignmentX(LEFT);
            previousPanel.add(previousCheckBox);

            JButton newFileButton = new JButton("Open arff files to build models from...");
            Dimension newFileButtonDimension = new Dimension(215, 29);
            newFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            newFileButton.setPreferredSize(newFileButtonDimension);
            newFileButton.setMaximumSize(newFileButtonDimension);
            newFileButton.setMinimumSize(newFileButtonDimension);
            newFileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openFileChooser();
                }
            });

            JPanel newPanel = new JPanel();
            newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.PAGE_AXIS));
            newPanel.setAlignmentX(LEFT);
            newPanel.add(newFileButton);

            JLabel orLabel = new JLabel("or");
            orLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            orLabel.setHorizontalAlignment(LEFT);

            JPanel optionsPanel = new JPanel(new BorderLayout());
            optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(10, 15, 35, 13)));
            optionsPanel.add(previousPanel, BorderLayout.PAGE_START);
            optionsPanel.add(orLabel, BorderLayout.CENTER);
            optionsPanel.add(newPanel, BorderLayout.PAGE_END);

            // execute panel
            Dimension executeButtonsDimension = new Dimension(145, 29);
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.setPreferredSize(executeButtonsDimension);
            startButton.setMaximumSize(executeButtonsDimension);
            startButton.setMinimumSize(executeButtonsDimension);
            startButton.setEnabled(false);
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startClassifiersTraining();
                }
            });

            jdlButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            jdlButton.setPreferredSize(executeButtonsDimension);
            jdlButton.setMaximumSize(executeButtonsDimension);
            jdlButton.setMinimumSize(executeButtonsDimension);
            jdlButton.setEnabled(false);
            jdlButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startJdlCreator();
                }
            });

            JPanel executePanel = new JPanel();
            executePanel.setLayout(new BoxLayout(executePanel, BoxLayout.PAGE_AXIS));
            executePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(15, 5, 15, 5)));
            executePanel.add(startButton);
            executePanel.add(Box.createRigidArea(new Dimension(160, 0)));
            executePanel.add(Box.createVerticalGlue());
            executePanel.add(jdlButton);

            // input panel
            JPanel inputPanel = new JPanel();
            inputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
            inputPanel.add(optionsPanel);
            inputPanel.add(Box.createRigidArea(new Dimension(10, 175)));
            inputPanel.add(executePanel);

            // title Label
            JLabel titleLabel = new JLabel("   Builds a classification model from the given arff files");
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // top panel
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.PAGE_AXIS));
            topPanel.add(titleLabel);
            topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            topPanel.add(inputPanel);
            topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // output panel
            outputText.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            outputText.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
            outputText.setTabSize(12);
            JScrollPane outputscroll = new JScrollPane(outputText);
            JPanel outputPanel = new JPanel(new BorderLayout());
            outputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
            outputPanel.add(outputscroll, BorderLayout.CENTER);

            // classifiers training panel
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(topPanel, BorderLayout.PAGE_START);
            add(outputPanel, BorderLayout.CENTER);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (Constants.Filename == null) {
                previousCheckBox.setSelected(false);
            } else {
                filename = Constants.Filename;
                pathfilename = Constants.PathFilename;
                N = Constants.N;
                pathfilenames = new String[N];
                for (int i = 0; i < N; i++) {
                    pathfilenames[i] = pathfilename.substring(0, pathfilename.lastIndexOf('.')) + "_" + (i + 1) + "_" + N + ".arff";
                    outputText.append("\nAdding input arff file: " + pathfilenames[i]);
                }
                startButton.setEnabled(true);
                jdlButton.setEnabled(true);
            }
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            startButton.setEnabled(false);
            jdlButton.setEnabled(false);
        }
    }

    public void openFileChooser() {
        JFileChooser newFileChooser = new JFileChooser(Constants.Path);
        CustomFileFilter arffFileFilter = new CustomFileFilter("arff");
        newFileChooser.setFileFilter(arffFileFilter);
        newFileChooser.setMultiSelectionEnabled(true);

        if (newFileChooser.showOpenDialog(ClassifierTrainingPanel.this) == JFileChooser.APPROVE_OPTION) {
            N = newFileChooser.getSelectedFiles().length;
            filename = newFileChooser.getSelectedFiles()[0].getName();
            if (filename.lastIndexOf('_') != -1) {
                filename = filename.substring(0, filename.lastIndexOf('_'));
                filename = filename.substring(0, filename.lastIndexOf('_')) + ".arff";
                pathfilename = newFileChooser.getSelectedFiles()[0].getAbsolutePath();
                pathfilename = pathfilename.substring(0, pathfilename.lastIndexOf('_'));
                pathfilename = pathfilename.substring(0, pathfilename.lastIndexOf('_')) + ".arff";
                pathfilenames = new String[N];
                for (int i = 0; i < N; i++) {
                    pathfilenames[i] = newFileChooser.getSelectedFiles()[i].getAbsolutePath();
                    outputText.append("\nAdding input arff file: " + pathfilenames[i]);
                }
            } else {
                outputText.append("\nIncompatible file naming.");
                outputText.append("\nFile naming pattern should be:  basefilename_i_N.arff");
                outputText.append("\nwhere N is the number of files and i is the index of each file.");
                outputText.append("\nNumbering should also be successive.");
            }
        }
        if (filename != null) {
            startButton.setEnabled(true);
            jdlButton.setEnabled(true);
        }
    }

    protected void startClassifiersTraining() {

        String trainFileName = null;
        String objectOutputFileName = null;
        String output[] = null;
        String results = "";
        int i1 = 0;
        try {
            outputText.append("\n\n");
            for (int i = 0; i < N; i++) {
                trainFileName = pathfilenames[i];
                objectOutputFileName = pathfilenames[i].substring(0, pathfilenames[i].lastIndexOf('.')) + ".model";
                long startTime = System.currentTimeMillis();
                output = EvaluationInternal.evaluateModel(new J48(), trainFileName, objectOutputFileName);
                outputText.append(output[0]);
                results += ("\nClassifier " + i1 + " training");
                results += (Total.showTime(System.currentTimeMillis() - startTime));
                results += (output[1]);
            }
            outputText.append("\n\nTraining of classification models with stratified cross-validation successful.\n");
            outputText.append("\n" + results);
            outputText.append("\n\n");
            Constants.Filename = filename;
            Constants.PathFilename = pathfilename;
            Constants.N = N;

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    protected void startJdlCreator() {
        JdlCreator jdlCreator = new JdlCreator();
        jdlCreator.setJDL_SHParameters(filename.substring(0, filename.lastIndexOf('.')), Constants.fileURL, N);
        jdlCreator.createTrainingJDL();
        jdlCreator.createTrainingSH();
        outputText.append("\n\nFile creation successful.");
        outputText.append("\n\nJDL and SH files have been created for all classifiers.");
        outputText.append("\n\n");
        Constants.Filename = filename;
        Constants.PathFilename = pathfilename;
        Constants.N = N;
    }
}
