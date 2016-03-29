
/**
 * <p>Title: ProteinClass.java</p>
 *
 * <p>Description: Handles protein classess.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


package GClass;


/**
 * Class for handling protein classes. <p>
 *
 * ------------------------------------------------------------------- <p>
 *
 * General options when evaluating the ClassifiersCombiner from the command-line: <p>
 *
 * -T filename <br>
 * Name of the file with the test data. <p>
 *
 */

public class ProteinClass {

  private String complexClassCode;
  private String[] classCodes;

  public ProteinClass(String singleClassCode) {
    classCodes = new String[1];
    classCodes[0] = singleClassCode;
    complexClassCode = singleClassCode;
  }

  public ProteinClass(String[] multipleClassCodes) {
    setClassCode(multipleClassCodes);
  }

  public void setClassCode(String[] classNames) {
    classCodes = classNames;
    complexClassCode = "";
    for (int i=0; i<classNames.length; i++) {
      complexClassCode = complexClassCode + classNames[i];
    }
  }

  public String getComplexClassCode() {
    return complexClassCode;
  }

  public boolean compareClasses(String[] externalClassCodes) {
    boolean check = true;
    if (classCodes.length == externalClassCodes.length) {
      for (int i=0; i<classCodes.length; i++) {
        boolean internalCheck = false;
        for (int j=0; j<externalClassCodes.length; j++) {
          if (classCodes[i].equalsIgnoreCase(externalClassCodes[j])) {
            internalCheck = true;
            break;
          }
        }
        if (internalCheck == false) {
          check = false;
        }
      }
    }
    else {
      check = false;
    }
    return check;
  }

}
