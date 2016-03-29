/**
 * <p>Title: </p>
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

package GClass.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Dimension;


public class BaseFrame extends JFrame {
    JPanel contentPane;
    BorderLayout borderLayout = new BorderLayout();
    JTabbedPane BasePane = new JTabbedPane();
    ArffCreatorPanel arffCreatorPanel = new ArffCreatorPanel();
    ArffSplitterPanel arffSplitterPanel = new ArffSplitterPanel();
    ClassifierTrainingPanel classifierTrainingPanel = new ClassifierTrainingPanel();
    ClassifierCombinerPanel classifierCombinerPanel = new ClassifierCombinerPanel();
    InstanceClassificationPanel instanceClassificationPanel = new InstanceClassificationPanel();
    JMenuBar jMenuBar = new JMenuBar();
    JMenu AboutMenu = new JMenu();

    public BaseFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayout);
        this.setJMenuBar(jMenuBar);
        setSize(700,550);
        setMinimumSize(new Dimension(700, 550));
        setTitle("G-Class");
        AboutMenu.setText("About");
        contentPane.add(BasePane, java.awt.BorderLayout.CENTER);
        BasePane.add(arffCreatorPanel, "  Arff Creation  ");
        BasePane.add(arffSplitterPanel, "  Arff Splitting  ");
        BasePane.add(classifierTrainingPanel, "  Classifier Training  ");
        BasePane.add(classifierCombinerPanel, "  Classifier Combining  ");
        BasePane.add(instanceClassificationPanel, "  Instance Classification  ");
        jMenuBar.add(AboutMenu);
    }
}
