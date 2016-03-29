/**
 *
 */

package GClass.gui;

import GClass.ArffCreator;
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
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.event.ChangeEvent;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.JCheckBox;

public class ArffCreatorPanel extends JPanel implements SwingConstants,
    ActionListener, ChangeListener, ListSelectionListener, ItemListener {

    protected JTextArea outputText = new JTextArea();
    protected JButton startButton = new JButton("Start");
    protected JButton jdlButton = new JButton("Create JDL and SH file");
    protected JPanel radioButtonsPanel = new JPanel();
    protected JRadioButton preset1 = new JRadioButton("1 class");
    protected JRadioButton preset10 = new JRadioButton("10 classes");
    protected JRadioButton preset20 = new JRadioButton("20 classes");
    protected JRadioButton preset30 = new JRadioButton("30 classes");
    protected JRadioButton preset1100 = new JRadioButton("All 1100 classes");
    protected JCheckBox presetsCheckBox = new JCheckBox("Choose one of the preset class sets");
    protected JCheckBox customsLargerCheckBox = new JCheckBox("Choose the number of larger classes");
    protected JCheckBox customsSpecificCheckBox = new JCheckBox("Choose the specific classes");
    protected JSpinner classSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1100, 1));
    protected JCheckBox listCheckBox = new JCheckBox("OK");
    protected JList customsClassList;
    protected String[] allClassesList = null;
    protected String[] classList = null;
    protected String[] options = new String[2];

    /**
     * Creates the panel
     */
    public ArffCreatorPanel() {

        try {
            // options panel
            ButtonGroup radioButtons = new ButtonGroup();
            radioButtons.add(preset1);
            radioButtons.add(preset10);
            radioButtons.add(preset20);
            radioButtons.add(preset30);
            radioButtons.add(preset1100);

            preset1.addActionListener(this);
            preset10.addActionListener(this);
            preset20.addActionListener(this);
            preset30.addActionListener(this);
            preset1100.addActionListener(this);

            preset1.setActionCommand("1");
            preset10.setActionCommand("10");
            preset20.setActionCommand("20");
            preset30.setActionCommand("30");
            preset1100.setActionCommand("1100");

            radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.PAGE_AXIS));
            radioButtonsPanel.add(preset1);
            radioButtonsPanel.add(preset10);
            radioButtonsPanel.add(preset20);
            radioButtonsPanel.add(preset30);
            radioButtonsPanel.add(preset1100);

            presetsCheckBox.addItemListener(this);

            JPanel presetsPanel = new JPanel();
            presetsPanel.setLayout(new BoxLayout(presetsPanel, BoxLayout.PAGE_AXIS));
            presetsPanel.setAlignmentX(LEFT);
            presetsPanel.add(presetsCheckBox);
            presetsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            presetsPanel.add(radioButtonsPanel);

            customsLargerCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            customsLargerCheckBox.addItemListener(this);
            JLabel customsLargerLabel = new JLabel("  to include in a custom class set");
            customsLargerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel classSpinnerPanel = new JPanel(new FlowLayout(LEADING));
            classSpinnerPanel.setMaximumSize(new Dimension(100, 30));
            classSpinnerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            classSpinnerPanel.add(classSpinner);
            classSpinner.addChangeListener(this);

            customsSpecificCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            customsSpecificCheckBox.addItemListener(this);
            JLabel customsSpecificLabel = new JLabel("  to include in a custom class set");
            customsSpecificLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            ArffCreator arffcreator = new ArffCreator();
            allClassesList = arffcreator.getDistinctClasses();
            customsClassList = new JList(allClassesList);
            customsClassList.setVisibleRowCount( -1);
            customsClassList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            customsClassList.addListSelectionListener(this);
            JScrollPane classListScrollPane = new JScrollPane(customsClassList);

            listCheckBox.addItemListener(this);

            JPanel classListPanel = new JPanel();
            classListPanel.setLayout(new BoxLayout(classListPanel, BoxLayout.LINE_AXIS));
            classListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            classListPanel.setMaximumSize(new Dimension(170, 20));
            classListPanel.add(Box.createRigidArea(new Dimension(5, 0)));
            classListPanel.add(classListScrollPane);
            classListPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            classListPanel.add(listCheckBox);

            JPanel customsPanel = new JPanel();
            customsPanel.setLayout(new BoxLayout(customsPanel, BoxLayout.PAGE_AXIS));
            customsPanel.add(customsLargerCheckBox);
            customsPanel.add(customsLargerLabel);
            customsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            customsPanel.add(classSpinnerPanel);
            customsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            customsPanel.add(customsSpecificCheckBox);
            customsPanel.add(customsSpecificLabel);
            customsPanel.add(Box.createRigidArea(new Dimension(0, 9)));
            customsPanel.add(classListPanel);

            JLabel orLabel = new JLabel("or", CENTER);
            orLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
            orLabel.setVerticalAlignment(TOP);

            JPanel optionsPanel = new JPanel(new BorderLayout());
            optionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(6, 10, 3, 13)));
            optionsPanel.add(presetsPanel, BorderLayout.LINE_START);
            optionsPanel.add(orLabel, BorderLayout.CENTER);
            optionsPanel.add(customsPanel, BorderLayout.LINE_END);

            // execute panel
            Dimension executeButtonsDimension = new Dimension(145, 29);
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startButton.setPreferredSize(executeButtonsDimension);
            startButton.setMaximumSize(executeButtonsDimension);
            startButton.setMinimumSize(executeButtonsDimension);
            startButton.setEnabled(false);
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startArffCreator();
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
            JLabel titleLabel = new JLabel("   Creates an arff file using the class set selected");
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

            // arff creator panel
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(topPanel, BorderLayout.PAGE_START);
            add(outputPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        options[0] = "-p";
        options[1] = e.getActionCommand();
        startButton.setEnabled(true);
    }

    public void stateChanged(ChangeEvent e) {
        options[0] = "-n";
        options[1] = classSpinner.getModel().getValue().toString();
        startButton.setEnabled(true);
    }

    public void valueChanged(ListSelectionEvent e) {
        listCheckBox.setSelected(false);
    }

    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        initializeAll();
        ListSelectionModel customsClassListModel = customsClassList.getSelectionModel();
        Vector classListVector = new Vector();
        String newClassElement = null;

        if (source == presetsCheckBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                customsLargerCheckBox.setSelected(false);
                customsSpecificCheckBox.setSelected(false);
                classSpinner.setEnabled(false);
                customsClassList.setEnabled(false);
                listCheckBox.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                classSpinner.setEnabled(true);
                customsClassListModel.clearSelection();
                customsClassList.setEnabled(true);
                listCheckBox.setEnabled(true);
                startButton.setEnabled(false);
            }
        }
        if (source == customsLargerCheckBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                presetsCheckBox.setSelected(false);
                customsSpecificCheckBox.setSelected(false);
                disableRadioButtons();
                customsClassList.setEnabled(false);
                listCheckBox.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                enableRadioButtons();
                customsClassListModel.clearSelection();
                customsClassList.setEnabled(true);
                listCheckBox.setEnabled(true);
                startButton.setEnabled(false);
            }
        }
        if (source == customsSpecificCheckBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                presetsCheckBox.setSelected(false);
                customsLargerCheckBox.setSelected(false);
                disableRadioButtons();
                classSpinner.setEnabled(false);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                enableRadioButtons();
                classSpinner.setEnabled(true);
                startButton.setEnabled(false);
            }
        }

        if (source == listCheckBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                outputText.append("\nClasses selected: ");
                int minIndex = customsClassListModel.getMinSelectionIndex();
                int maxIndex = customsClassListModel.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (customsClassListModel.isSelectedIndex(i)) {
                        newClassElement = allClassesList[i];
                        classListVector.addElement(newClassElement);
                        outputText.append("\n" + newClassElement);
                    }
                }
                classList = new String[classListVector.size()];
                for (int i = 0; i < classListVector.size(); i++) {
                    classList[i] = (String) classListVector.elementAt(i);
                }
                if (!classListVector.isEmpty()) {
                    startButton.setEnabled(true);
                } else {
                    outputText.append("\nNo classes selected!");
                }
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                customsClassListModel.clearSelection();
            }
        }
    }

    public void disableRadioButtons() {
        preset1.setSelected(false);
        preset10.setSelected(false);
        preset20.setSelected(false);
        preset30.setSelected(false);
        preset1100.setSelected(false);
        preset1.setEnabled(false);
        preset10.setEnabled(false);
        preset20.setEnabled(false);
        preset30.setEnabled(false);
        preset1100.setEnabled(false);
    }

    public void enableRadioButtons() {
        preset1.setEnabled(true);
        preset10.setEnabled(true);
        preset20.setEnabled(true);
        preset30.setEnabled(true);
        preset1100.setEnabled(true);
    }

    public void initializeAll() {
        classList = null;
        options = null;
        options = new String[2];
    }

    protected void startArffCreator() {

        try {
            Constants.Filename = Constants.PathFilename = null;
            long startTime = System.currentTimeMillis();
            ArffCreator creator = new ArffCreator();
            if (classList == null) {
                try {
                    classList = creator.classList(options);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                creator.setFilename("genbase_custom" + classList.length + classList[0] + ".arff");
            }
            if (classList != null) {
                String[] proteinList = creator.getProteinsForClassList(classList);
                String[] allMotifs = creator.getDistinctMotifs();

                outputText.append("\n\nNumber of Classes: " + classList.length);
                outputText.append("\nNumber of Motifs: " + allMotifs.length);
                outputText.append("\nNumber of Proteins: " + proteinList.length);

                Vector actualClassesVec = new Vector(1, 1);
                for (int i = 0; i < proteinList.length; i++) {
                    String currentProtein = proteinList[i];
                    String[] currentProteinClasses = creator.getProteinClasses(currentProtein);
                    for (int j = 0; j < currentProteinClasses.length; j++) {
                        String currentClass = currentProteinClasses[j];
                        int flag = 0;
                        for (int k = 0; k < actualClassesVec.size(); k++) {
                            if (currentClass.equalsIgnoreCase(actualClassesVec.elementAt(k).toString())) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            actualClassesVec.addElement(currentClass);
                        }
                    }
                }

                String[] actualClasses = new String[actualClassesVec.size()];
                for (int i = 0; i < actualClassesVec.size(); i++) {
                    actualClasses[i] = actualClassesVec.elementAt(i).toString();
                }

                try {
                    FileOutputStream fout = new FileOutputStream(creator.getFilename());
                    PrintStream myOutput = new PrintStream(fout);
                    myOutput.println("@relation protein\n");
                    myOutput.print("@attribute protein {");
                    for (int i = 0; i < proteinList.length; i++) {
                        if (i == proteinList.length - 1) {
                            myOutput.print(proteinList[i]);
                        } else {
                            myOutput.print(proteinList[i] + ", ");
                        }
                    }
                    myOutput.println("}");
                    for (int i = 0; i < allMotifs.length; i++) {
                        myOutput.println("@attribute " + allMotifs[i] + " {YES,NO}");
                    }

                    myOutput.print("@attribute CLASS {");
                    for (int i = 0; i < actualClasses.length; i++) {
                        if (i == actualClasses.length - 1) {
                            myOutput.print(actualClasses[i]);
                        } else {
                            myOutput.print(actualClasses[i] + ", ");
                        }
                    }
                    myOutput.println("}\n");

                    myOutput.println("@data");
                    for (int i = 0; i < proteinList.length; i++) {
                        String currentProtein = proteinList[i];
                        String[] currentProteinMotifs = creator.getProteinMotifs(currentProtein);
                        String[] currentProteinClasses = creator.getProteinClasses(currentProtein);
                        for (int j = 0; j < currentProteinClasses.length; j++) {
                            myOutput.print(currentProtein + ", ");
                            for (int k = 0; k < allMotifs.length; k++) {
                                int flag = 0;
                                for (int l = 0; l < currentProteinMotifs.length; l++) {
                                    if (allMotifs[k].equalsIgnoreCase(currentProteinMotifs[l])) {
                                        myOutput.print("YES, ");
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    myOutput.print("NO, ");
                                }
                            }
                            myOutput.println(currentProteinClasses[j]);
                        }
                        Constants.Filename = Constants.PathFilename = creator.getFilename();
                    }
                } catch (IOException exp) {
                    outputText.append("\nError: " + exp);
                    exp.printStackTrace();
                }
                outputText.append("\n\nArff file creation successful.");
                outputText.append("\n\nArffCreator" + Total.showTime(System.currentTimeMillis() - startTime));
                outputText.append("\n\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
