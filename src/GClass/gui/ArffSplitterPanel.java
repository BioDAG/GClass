/**
 *
 */

package GClass.gui;

import GClass.ArffSplitter;
import GClass.JdlCreator;
import GClass.Total;

import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;

public class ArffSplitterPanel extends JPanel implements SwingConstants, ChangeListener, ItemListener {

    protected JTextArea outputText = new JTextArea();
    protected JButton startButton = new JButton("Start");
    protected JButton jdlButton = new JButton("Create JDL and SH file");
    protected JCheckBox previousCheckBox = new JCheckBox("Use the arff file created in the previous panel");
    protected JSpinner splitsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1100, 1));
    protected String filename = null;
    protected String pathfilename = null;

    /**
     * Creates the panel
     */
    public ArffSplitterPanel() {

        try {
            // options panel
            previousCheckBox.addItemListener(this);

            JPanel previousPanel = new JPanel();
            previousPanel.setLayout(new BoxLayout(previousPanel, BoxLayout.PAGE_AXIS));
            previousPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            previousPanel.add(previousCheckBox);

            JButton newFileButton = new JButton("Open new arff file to split...");
            Dimension newFileButtonDimension = new Dimension(175, 29);
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
            newPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            newPanel.add(newFileButton);

            JLabel andLabel = new JLabel("  and");
            andLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel splitsLabel = new JLabel("  choose the number of splits");
            splitsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel splitsSpinnerPanel = new JPanel(new FlowLayout(LEADING));
            splitsSpinnerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            splitsSpinnerPanel.add(splitsSpinner);
            splitsSpinner.setEnabled(false);
            splitsSpinner.addChangeListener(this);

            JPanel splitsPanel = new JPanel();
            splitsPanel.setLayout(new BoxLayout(splitsPanel, BoxLayout.PAGE_AXIS));
            splitsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            splitsPanel.add(andLabel);
            splitsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            splitsPanel.add(splitsLabel);
            splitsPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            splitsPanel.add(splitsSpinnerPanel);

            JPanel newsplitsPanel = new JPanel();
            newsplitsPanel.setLayout(new BoxLayout(newsplitsPanel, BoxLayout.PAGE_AXIS));
            newsplitsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            newsplitsPanel.add(newPanel);
            newsplitsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            newsplitsPanel.add(splitsPanel);

            JLabel orLabel = new JLabel("or");
            orLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            orLabel.setHorizontalAlignment(LEFT);

            JPanel optionsPanel = new JPanel(new BorderLayout());
            optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(6, 15, 3, 13)));
            optionsPanel.add(previousPanel, BorderLayout.PAGE_START);
            optionsPanel.add(orLabel, BorderLayout.CENTER);
            optionsPanel.add(newsplitsPanel, BorderLayout.PAGE_END);

            // execute panel
            Dimension executeButtonsDimension = new Dimension(145, 29);
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.setPreferredSize(executeButtonsDimension);
            startButton.setMaximumSize(executeButtonsDimension);
            startButton.setMinimumSize(executeButtonsDimension);
            startButton.setEnabled(false);
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startArffSplitter();
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
            JLabel titleLabel = new JLabel("   Splits an arff file into the number of splits selected");
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
            JScrollPane outputscroll = new JScrollPane(outputText);
            JPanel outputPanel = new JPanel(new BorderLayout());
            outputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
            outputPanel.add(outputscroll, BorderLayout.CENTER);

            // arff splitter panel
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
                previousCheckBox.setEnabled(false);
            } else {
                File file = new File(Constants.Filename);
                filename = Constants.Filename;
                pathfilename = file.getAbsolutePath();
                outputText.append("\nInput arff file: " + pathfilename);
                splitsSpinner.setEnabled(true);
            }
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            splitsSpinner.setEnabled(false);
        }
    }

    public void openFileChooser() {
        JFileChooser newFileChooser = new JFileChooser(Constants.Path);
        CustomFileFilter arffFileFilter = new CustomFileFilter("arff");
        newFileChooser.setFileFilter(arffFileFilter);

        if (newFileChooser.showOpenDialog(ArffSplitterPanel.this) == JFileChooser.APPROVE_OPTION) {
            filename = newFileChooser.getSelectedFile().getName();
            pathfilename = newFileChooser.getSelectedFile().getAbsolutePath();
            outputText.append("\nFile chosen: " + pathfilename);
        }
        if (filename != null) {
            splitsSpinner.setEnabled(true);
        }
    }

    public void stateChanged(ChangeEvent e) {
        Constants.N = ( (SpinnerNumberModel) splitsSpinner.getModel()).getNumber().intValue();
        startButton.setEnabled(true);
        jdlButton.setEnabled(true);
    }

    protected void startArffSplitter() {
        long startTime = System.currentTimeMillis();
        ArffSplitter splitter = new ArffSplitter(Constants.N, pathfilename);
        try {
            splitter.split();
            outputText.append("\n\nArff file stratified splitting successful.");
            outputText.append("\n\nArffSplitter" + Total.showTime(System.currentTimeMillis() - startTime));
            outputText.append("\n\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Constants.Filename = filename;
        Constants.PathFilename = pathfilename;
    }

    protected void startJdlCreator() {
        JdlCreator jdlCreator = new JdlCreator();
        jdlCreator.setJDL_SHParameters(filename.substring(0, filename.lastIndexOf('.')), Constants.fileURL, Constants.N);
        jdlCreator.createArffSplitterJDL();
        jdlCreator.createArffSplitterSH();
        outputText.append("\n\nFile creation successful.");
        outputText.append("\n\nFiles arffSplitter.jdl and arffSplitter.sh have been created.");
        outputText.append("\n\n");
        Constants.Filename = filename;
        Constants.PathFilename = pathfilename;
    }
}
