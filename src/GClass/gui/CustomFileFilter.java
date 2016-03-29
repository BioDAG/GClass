package GClass.gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

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
public class CustomFileFilter extends FileFilter{

    String ExtensionString = null;

    public CustomFileFilter(String extensionString) {
        ExtensionString = extensionString;
    }

    public String getDescription() {
        return ExtensionString + " files";
    }

    public boolean accept(File file) {
        String extension = null;
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i > 0 && i < name.length() - 1) {
            extension = name.substring(i + 1).toLowerCase();
        }
        if (file.isDirectory()) {
            return true;
        } else if (extension != null) {
            if (extension.equals(ExtensionString)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
