/**
 *
 */

package GClass.gui;

import GClass.ClassifierCombiner;
import GClass.CombinerEvaluation;
import GClass.Total;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractAction;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;

public class InstanceClassificationPanel extends JPanel implements SwingConstants, ItemListener,
    PropertyChangeListener, ChangeListener {

    protected JTextArea outputText = new JTextArea();
    protected JButton startButton = new JButton("Start");
    protected JButton jdlButton = new JButton("Create JDL and SH file");
    protected JCheckBox previousCheckBox = new JCheckBox("Use the model files created in the previous panel");
    protected JFileChooser newFileChooser = new JFileChooser();
    protected JFileChooser testFileChooser = new JFileChooser();
    protected JSlider thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
    protected JFormattedTextField thresholdTextField;
    protected double threshold = 0;
    protected String filename = null;
    protected String pathfilename = null;
    protected String[] modelsString = null;
    protected int N = 0;

    /**
     * Creates the panel
     */
    public InstanceClassificationPanel() {

        try {
            // options panel
            previousCheckBox.addItemListener(this);

            JPanel previousPanel = new JPanel();
            previousPanel.setLayout(new BoxLayout(previousPanel, BoxLayout.PAGE_AXIS));
            previousPanel.setAlignmentX(Component.LEFT_ALIGNMENT); ;
            previousPanel.add(previousCheckBox);

            JButton newFileButton = new JButton("Open model files to combine...");
            Dimension newFileButtonDimension = new Dimension(180, 29);
            newFileButton.setPreferredSize(newFileButtonDimension);
            newFileButton.setMaximumSize(newFileButtonDimension);
            newFileButton.setMinimumSize(newFileButtonDimension);
            newFileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openFileChooser();
                }
            });

            JButton testFileButton = new JButton("Open arff file for classification...");
            Dimension testFileButtonDimension = new Dimension(190, 29);
            testFileButton.setPreferredSize(testFileButtonDimension);
            testFileButton.setMaximumSize(testFileButtonDimension);
            testFileButton.setMinimumSize(testFileButtonDimension);
            testFileButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openTestFileChooser();
                }
            });

            JPanel FilesPanel = new JPanel();
            FilesPanel.setLayout(new BoxLayout(FilesPanel, BoxLayout.LINE_AXIS));
            FilesPanel.setAlignmentX(Component.LEFT_ALIGNMENT); ;
            FilesPanel.add(newFileButton);
            FilesPanel.add(Box.createRigidArea(new Dimension(40, 0)));
            FilesPanel.add(testFileButton);

            NumberFormat thresholdNumberFormat = java.text.NumberFormat.getIntegerInstance();
            NumberFormatter thresholdNumberFormatter = new NumberFormatter(thresholdNumberFormat);
            thresholdNumberFormatter.setMinimum(new Integer(0));
            thresholdNumberFormatter.setMaximum(new Integer(100));

            thresholdTextField = new JFormattedTextField(thresholdNumberFormatter);
            thresholdTextField.setPreferredSize(new Dimension(30, 18));
            thresholdTextField.setMaximumSize(new Dimension(30, 18));
            thresholdTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
            thresholdTextField.setHorizontalAlignment(JTextField.RIGHT);
            thresholdTextField.setValue(new Integer(1));
            thresholdTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
            thresholdTextField.getActionMap().put("check", new setThreshold());
            thresholdTextField.addPropertyChangeListener(this);

            JPanel thresholdLabelTextPanel = new JPanel();
            thresholdLabelTextPanel.setLayout(new BoxLayout(thresholdLabelTextPanel, BoxLayout.LINE_AXIS));
            thresholdLabelTextPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            thresholdLabelTextPanel.add(new JLabel("  Set threshold for instance classification probabilities    "));
            thresholdLabelTextPanel.add(thresholdTextField);
            thresholdLabelTextPanel.add(new JLabel(" %"));

            thresholdSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
            thresholdSlider.setPreferredSize(new Dimension(310, 40));
            thresholdSlider.setMaximumSize(new Dimension(310, 40));
            thresholdSlider.setSnapToTicks(true);
            thresholdSlider.setMajorTickSpacing(10);
            thresholdSlider.setMinorTickSpacing(1);
            thresholdSlider.setPaintTicks(true);
            thresholdSlider.setPaintLabels(true);
            thresholdSlider.addChangeListener(this);

            JPanel thresholdPanel = new JPanel();
            thresholdPanel.setLayout(new BoxLayout(thresholdPanel, BoxLayout.PAGE_AXIS));
            thresholdPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            thresholdPanel.add(thresholdLabelTextPanel);
            thresholdPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            thresholdPanel.add(thresholdSlider);

            JPanel newPanel = new JPanel();
            newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.PAGE_AXIS));
            newPanel.setAlignmentX(Component.LEFT_ALIGNMENT); ;
            newPanel.add(FilesPanel);
            newPanel.add(Box.createRigidArea(new Dimension(0, 14)));
            newPanel.add(thresholdPanel);

            JLabel orLabel = new JLabel("or");
            orLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            orLabel.setHorizontalAlignment(LEFT);

            JPanel optionsPanel = new JPanel(new BorderLayout());
            optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(6, 15, 7, 13)));
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
                    startClassifiersCombiner();
                }
            });

            jdlButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            jdlButton.setPreferredSize(executeButtonsDimension);
            jdlButton.setMaximumSize(executeButtonsDimension);
            jdlButton.setMinimumSize(executeButtonsDimension);
            jdlButton.setEnabled(false);

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
            JLabel titleLabel = new JLabel("   Combines the predictions of classification models given");
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

            // instances classification panel
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
                modelsString = new String[N + 1];
                modelsString[0] = pathfilename;
                for (int i = 0; i < N; i++) {
                    modelsString[i + 1] = pathfilename.substring(0, pathfilename.lastIndexOf('.')) + "_" + (i + 1) + "_" + N + ".model";
                    outputText.append("\nAdding classification model file: " + modelsString[i + 1]);
                }
            }
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            startButton.setEnabled(false);
            jdlButton.setEnabled(false);
        }
    }

    public void openFileChooser() {
        JFileChooser newFileChooser = new JFileChooser(Constants.Path);
        CustomFileFilter modelFileFilter = new CustomFileFilter("model");
        newFileChooser.setFileFilter(modelFileFilter);
        newFileChooser.setMultiSelectionEnabled(true);

        if (newFileChooser.showOpenDialog(InstanceClassificationPanel.this) == JFileChooser.APPROVE_OPTION) {
            N = newFileChooser.getSelectedFiles().length;
            modelsString = new String[N + 1];
            for (int i = 0; i < N; i++) {
                modelsString[i + 1] = newFileChooser.getSelectedFiles()[i].getAbsolutePath();
                outputText.append("\nAdding classification model file: " + modelsString[i + 1]);
            }
        }
    }

    public void openTestFileChooser() {

        if (N == 0) {
            outputText.append("\nNo model files selected.");
            outputText.append("\nModel files must be set first,");
            outputText.append("either by using the previously generated files, or by open new ones.");
        } else {
            JFileChooser newFileChooser = new JFileChooser(Constants.Path);
            CustomFileFilter modelFileFilter = new CustomFileFilter("arff");
            newFileChooser.setFileFilter(modelFileFilter);

            if (newFileChooser.showOpenDialog(InstanceClassificationPanel.this) == JFileChooser.APPROVE_OPTION) {
                N = newFileChooser.getSelectedFiles().length;
                filename = newFileChooser.getSelectedFile().getName() + ".arff";
                pathfilename = newFileChooser.getSelectedFile().getAbsolutePath() + ".arff"; ;
                modelsString[0] = pathfilename;
                outputText.append("\nOpening file for classification: " + pathfilename);
            }
            if (filename != null) {
                startButton.setEnabled(true);
                jdlButton.setEnabled(true);
            }
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        if ("value".equals(e.getPropertyName())) {
            Number value = (Number) e.getNewValue();
            if (thresholdSlider != null && value != null) {
                thresholdSlider.setValue(value.intValue());
                threshold = value.doubleValue() / 100;
            }
        }
    }

    public void stateChanged(ChangeEvent e) {
        JSlider sliderSource = (JSlider) e.getSource();
        int sliderValue = (int) sliderSource.getValue();
        if (!sliderSource.getValueIsAdjusting()) {
            thresholdTextField.setValue(new Integer(sliderValue));
            threshold = (double) sliderValue / 100;
        } else {
            thresholdTextField.setText(String.valueOf(sliderValue));
        }
    }

    public class setThreshold extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if (!thresholdTextField.isEditValid()) {
                Toolkit.getDefaultToolkit().beep();
                thresholdTextField.selectAll();
            } else {
                try {
                    thresholdTextField.commitEdit();
                } catch (java.text.ParseException exc) {}
            }
        }
    }

    protected void startClassifiersCombiner() {

        String output[] = null;
        try {
            outputText.append("\n\n");
            long startTime = System.currentTimeMillis();
            output = CombinerEvaluation.evaluateModelInternal(new ClassifierCombiner(), modelsString, threshold);
            outputText.append(output[0]);
            outputText.append(output[2]);
            outputText.append("\n\nClassification of instances successful.\n");
            outputText.append("\n\nClassifiersCombiner");
            outputText.append(Total.showTime(System.currentTimeMillis() - startTime));
            outputText.append(output[1]);
            outputText.append("\n\n");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
