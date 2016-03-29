/**
 * <p>Title: ArffCreator.java</p>
 *
 * <p>Description: Creates an arff file.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


package GClass;

import java.sql.*;
import java.io.*;
import java.util.*;
import weka.core.Utils;

/**
 * Class for creating the arff files. <p>
 *
 * ------------------------------------------------------------------- <p>
 *
 * Valid options from the command line are (set only one) :<p>
 *
 * -n number of classes <br>
 * The number of classes for a custom made list of the top larger classes. <p>
 *
 * -p number of classes from the preset class lists
 * The number of classes specifying which preset class list to use. <p>
 *
 */

public class ArffCreator {

    /** The preset class lists. */
    private static final String[] Classes1 = {"PDOC50086"};
    private static final String[] Classes10 = {
        "PDOC00064", "PDOC00154", "PDOC00224", "PDOC00271", "PDOC00343",
        "PDOC00561", "PDOC00662", "PDOC00670", "PDOC00791", "PDOC50007"};
    private static final String[] Classes20 = {
        "PDOC00020", "PDOC00023", "PDOC00027", "PDOC00335", "PDOC00340",
        "PDOC00344", "PDOC00552", "PDOC00561", "PDOC00564", "PDOC00567",
        "PDOC00789", "PDOC00790", "PDOC00793", "PDOC00800", "PDOC00803",
        "PDOC50001", "PDOC50006", "PDOC50017"};
    private static final String[] Classes30 = {
        "PDOC00010", "PDOC00013", "PDOC00021", "PDOC00024", "PDOC00027",
        "PDOC00031", "PDOC00061", "PDOC00067", "PDOC00072", "PDOC00075",
        "PDOC00160", "PDOC00164", "PDOC00166", "PDOC00167", "PDOC00171",
        "PDOC00298", "PDOC00300", "PDOC00304", "PDOC00305", "PDOC00307",
        "PDOC00310", "PDOC00485", "PDOC00486", "PDOC00490", "PDOC00495",
        "PDOC00499", "PDOC00722", "PDOC00728"};

    /** The filename of the output arff file. */
    private String filename = null;

    /**
     * Make up the help string giving all the command line options
     *
     * @return a string detailing the valid command line options
     */
    protected static String makeOptionString() {

        StringBuffer optionsText = new StringBuffer("");

        optionsText.append("\n\nArffCreator options (set only one) :\n\n");
        optionsText.append("-n <the number of classes>\n");
        optionsText.append("\tSets the number of classes for a custom made list of the top larger classes.\n\n");
        optionsText.append("-p <the number of classes>\n");
        optionsText.append("\tSets the number of classes to specify which preset class list to use.\n");
        optionsText.append("Available preset class lists:\n");
        optionsText.append("1    : a class list containing one class, the class " + Classes1[0] + ".\n");
        optionsText.append("10   : a class list containing the 10 most \"important\" classes.\n");
        optionsText.append("20   : a class list containing the 20 most \"important\" classes.\n");
        optionsText.append("30   : a class list containing the 30 most \"important\" classes.\n");
        optionsText.append("1100 : a class list containing all classes.\n");

        return optionsText.toString();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String Filename) {
        filename = Filename;
    }

    /**
     * Parses a given list of options, chooses and creates the appropriate class list.
     * Valid options are (set only one) :<p>
     *
     * -n number of classes <br>
     * The number of classes for a custom made list of the top larger classes. <p>
     *
     * -p number of classes from the preset class lists
     * The number of classes specifying which preset class list to use. <p>
     *
     * @return the string containing the class list
     * @param options the list of options as an array of strings
     * @exception Exception if an option is not supported
     */
    public String[] classList(String[] options) throws Exception {

        int n = 0;
        String[] classList = null;
        int presetClassList = 0;
        try {
            String nString = Utils.getOption('n', options);
            String presetClassListString = Utils.getOption('p', options);
            if (nString.length() != 0) {
                if (presetClassListString.length() != 0) {
                    throw new Exception(
                        "Only one option should be set.");
                } else {
                    n = Integer.parseInt(nString);
                    if (n == 0) {
                        throw new Exception(
                            "Meaningless operation. Number of classes is set to 0.");
                    } else if ( (n < 0) || (n > 1100)) {
                        throw new Exception(
                            "Number of classes is set out of bounds. Should be: 0 < n < 1100.");
                    } else {
                        classList = getLargeClasses(n);
                        filename = "genbase_larger" + n + ".arff";
                    }
                }
            } else {
                if (presetClassListString.length() != 0) {
                    presetClassList = Integer.parseInt(presetClassListString);
                    switch (presetClassList) {
                        case 1:
                            classList = Classes1;

                            //System.out.println("Class Name = " + classList[0]);
                            break;
                        case 10:
                            classList = Classes10;
                            break;
                        case 20:
                            classList = Classes20;
                            break;
                        case 30:
                            classList = Classes30;
                            break;
                        case 1100:
                            classList = getDistinctClasses();
                            break;
                        default:
                            throw new Exception(
                                "Incorrect number given via the -p option.");
                    }
                    filename = "genbase_preset" + presetClassList + ".arff";
                } else {
                    throw new Exception(
                        "One option has to be set.");
                }
            }

        } catch (Exception e) {
            throw new Exception("\nOptions Error: " + e.getMessage() + makeOptionString());
        }
        return classList;
    }

    /**
     * Gets a connection from the properties specified
     * in the file database.properties
     *
     * @return the database connection
     */
    public Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("database.properties");
        props.load(in);
        in.close();

        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) {
            System.setProperty("jdbc.drivers", drivers);
        }
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Gets a list of all the classes from the database
     *
     * @return the string containing the list
     */
    public String[] getDistinctClasses() {
        String[] classCodes = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT COUNT(DISTINCT [Class Code]) FROM [GenBase].[dbo].[Prosite]");
            result.next();
            int numberOfClasses = Integer.parseInt(result.getString(1));
            classCodes = new String[numberOfClasses];
            result = stat.executeQuery("SELECT DISTINCT [Class Code] FROM [GenBase].[dbo].[Prosite] ORDER BY [Class Code]");
            int pos = 0;
            while (result.next()) {
                classCodes[pos] = result.getString(1);
                pos++;
            }
            result.close();
            stat.close();
            conn.close();
            return classCodes;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return classCodes;
        } catch (IOException ex) {
            ex.printStackTrace();
            return classCodes;
        }
    }

    /**
     * Gets a list of the N larger classes from the database
     *
     * @return the string containing the list
     * @param n the number of classes needed
     */
    public String[] getLargeClasses(int n) {
        String[] classCodes = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            String statement = "SELECT TOP " + n + " * FROM (SELECT DISTINCT [Class Code],";
            statement += " (SELECT COUNT([Class Code]) FROM [GenBase].[dbo].[Prosite] TABLE2";
            statement += " WHERE (TABLE2.[Class Code] = TABLE1.[Class Code])) AS Expr1";
            statement += " FROM [GenBase].[dbo].[Prosite] TABLE1) DERIVEDTBL ORDER BY Expr1 DESC";
            classCodes = new String[n];
            ResultSet result = stat.executeQuery(statement);
            int pos = 0;
            while (result.next()) {
                classCodes[pos] = result.getString(1);
                pos++;
            }
            result.close();
            stat.close();
            conn.close();
            return classCodes;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return classCodes;
        } catch (IOException ex) {
            ex.printStackTrace();
            return classCodes;
        }
    }

    /**
     * Gets a list of all the motifs from the database
     *
     * @return the string containing the list
     */
    public String[] getDistinctMotifs() {
        String[] motifs = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT COUNT(*) FROM [GenBase].[dbo].[AllMotifs]");
            result.next();
            int numberOfMotifs = Integer.parseInt(result.getString(1));
            motifs = new String[numberOfMotifs];
            result = stat.executeQuery("SELECT [Motifs] FROM [GenBase].[dbo].[AllMotifs] ORDER BY [Motifs]");
            int pos = 0;
            while (result.next()) {
                motifs[pos] = result.getString(1);
                pos++;
            }
            result.close();
            stat.close();
            conn.close();
            return motifs;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return motifs;
        } catch (IOException ex) {
            ex.printStackTrace();
            return motifs;
        }
    }

    /**
     * Gets a list of all the proteins from the database
     *
     * @return the string containing the list
     */
    public String[] getDistinctProteins() {
        String[] proteins = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT COUNT(DISTINCT [Protein Code]) FROM [GenBase].[dbo].[Prosite] WHERE [Flag] = 'T'");
            result.next();
            int numberOfProteins = Integer.parseInt(result.getString(1));
            proteins = new String[numberOfProteins];
            result = stat.executeQuery("SELECT DISTINCT [Protein Code] FROM [GenBase].[dbo].[Prosite] WHERE [Flag] = 'T' ORDER BY [Protein Code]");
            int pos = 0;
            while (result.next()) {
                proteins[pos] = result.getString(1);
                pos++;
            }
            result.close();
            stat.close();
            conn.close();
            return proteins;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return proteins;
        } catch (IOException ex) {
            ex.printStackTrace();
            return proteins;
        }
    }

    /**
     * Gets a list of all the motifs of a specific protein
     *
     * @return the string containing the list
     * @param proteinCode the string containing the protein
     */
    public String[] getProteinMotifs(String proteinCode) {
        String[] proteinMotifs = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT COUNT(*) FROM [GenBase].[dbo].[Pattern] WHERE  [Protein Code] = '" + proteinCode + "'");
            result.next();
            int numberOfMotifs = Integer.parseInt(result.getString(1));
            result = stat.executeQuery("SELECT COUNT(*) FROM [GenBase].[dbo].[Profile] WHERE [Protein Code] = '" + proteinCode + "'");
            result.next();
            numberOfMotifs += Integer.parseInt(result.getString(1));
            proteinMotifs = new String[numberOfMotifs];
            result = stat.executeQuery("SELECT [Pattern Code] FROM [GenBase].[dbo].[Pattern] WHERE [Protein Code] = '" + proteinCode + "'");
            int pos = 0;
            while (result.next()) {
                proteinMotifs[pos] = result.getString(1);
                pos++;
            }
            result = stat.executeQuery("SELECT [Profile Code] FROM [GenBase].[dbo].[Profile] WHERE [Protein Code] = '" + proteinCode + "'");
            while (result.next()) {
                proteinMotifs[pos] = result.getString(1);
                pos++;
            }
            result.close();
            stat.close();
            conn.close();
            return proteinMotifs;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return proteinMotifs;
        } catch (IOException ex) {
            ex.printStackTrace();
            return proteinMotifs;
        }
    }

    /**
     * Gets a list of all the classes of a specific protein
     *
     * @return the string containing the list
     * @param proteinCode the string containing the protein
     */
    public String[] getProteinClasses(String proteinCode) {
        String[] proteinClasses = null;
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery("SELECT COUNT(*) FROM [GenBase].[dbo].[Prosite] WHERE [Flag] = 'T' AND [Protein Code] = '" + proteinCode + "'");
            result.next();
            int numberOfClasses = Integer.parseInt(result.getString(1));
            proteinClasses = new String[numberOfClasses];
            result = stat.executeQuery("SELECT [Class Code] FROM [GenBase].[dbo].[Prosite] WHERE [Flag] = 'T' AND [Protein Code] = '" + proteinCode + "'");
            int pos = 0;
            while (result.next()) {
                proteinClasses[pos] = result.getString(1);
                pos++;
            }
            result.close();
            stat.close();
            conn.close();
            return proteinClasses;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return proteinClasses;
        } catch (IOException ex) {
            ex.printStackTrace();
            return proteinClasses;
        }
    }

    /**
     * Gets a list of all the proteins in the classes of a class list
     *
     * @return the string containing the list
     * @param classList the string containing the class list
     */
    public String[] getProteinsForClassList(String[] classList) {
        String[] proteins = null;
        try {
            String sqlQueryCOunt = "SELECT COUNT(DISTINCT [Protein Code]) FROM [GenBase].[dbo].[Prosite] WHERE [Flag] = 'T' AND (";
            for (int i = 0; i < classList.length; i++) {
                if (i == classList.length - 1) {
                    sqlQueryCOunt = sqlQueryCOunt + "[Class Code] = '" + classList[i] + "')";
                } else {
                    sqlQueryCOunt = sqlQueryCOunt + "[Class Code] = '" + classList[i] + "' OR ";
                }
            }
            String sqlQuery = "SELECT DISTINCT [Protein Code] FROM [GenBase].[dbo].[Prosite] WHERE [Flag] = 'T' AND (";
            for (int i = 0; i < classList.length; i++) {
                if (i == classList.length - 1) {
                    sqlQuery = sqlQuery + "[Class Code] = '" + classList[i] + "')";
                } else {
                    sqlQuery = sqlQuery + "[Class Code] = '" + classList[i] + "' OR ";
                }
            }
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            ResultSet result = stat.executeQuery(sqlQueryCOunt);
            result.next();
            int numberOfProteins = Integer.parseInt(result.getString(1));
            proteins = new String[numberOfProteins];
            result = stat.executeQuery(sqlQuery);
            int pos = 0;
            while (result.next()) {
                proteins[pos] = result.getString(1);
                pos++;
            }
            result.close();
            stat.close();
            conn.close();
            return proteins;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
            return proteins;
        } catch (IOException ex) {
            ex.printStackTrace();
            return proteins;
        }
    }

    /**  ???
     * Creates a list of all the classes of the proteins
     * of the classes of a class list and saves them in a separate file ...
     *
     * @return a vector containing the classes
     * @param proteinCodeList the protein list ...
     * @param simpleClassCideList the class list ...
     * @param filename the string containing the filename ...
     */
    public Vector createClassCodeList(String[] proteinCodeList, String[] simpleClassCideList, String filename) {
        Vector classCodes = new Vector(1, 1);
        for (int i = 0; i < simpleClassCideList.length; i++) {
            ProteinClass newClass = new ProteinClass(simpleClassCideList[i]);
            classCodes.add(newClass);
        }

        for (int i = 0; i < proteinCodeList.length; i++) {
            System.out.println("Processing Protein No" + (i + 1) + " from " + proteinCodeList.length + "...");
            String[] proteinClassCodes = getProteinClasses(proteinCodeList[i]);
            boolean found = false;
            for (int j = 0; j < classCodes.size(); j++) {
                ProteinClass currentClass = (ProteinClass) classCodes.elementAt(j);
                if (currentClass.compareClasses(proteinClassCodes)) {
                    System.out.println(currentClass.getComplexClassCode());
                    found = true;
                    break;
                }
            }
            if (found == false) {
                ProteinClass newClass = new ProteinClass(proteinClassCodes);
                classCodes.add(newClass);
                System.out.println(newClass.getComplexClassCode());
            }
        }

        try {
            FileOutputStream fout = new FileOutputStream(filename);
            PrintStream myOutput = new PrintStream(fout);
            myOutput.println(classCodes.size());
            for (int i = 0; i < classCodes.size(); i++) {
                ProteinClass temp = (ProteinClass) classCodes.elementAt(i);
                myOutput.println(temp.getComplexClassCode());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        return classCodes;
    }

    /**
     * Main method.
     *
     * @param options should contain one of the following options:
     * -n number of classes
     * -p number of classes from the preset class lists
     */
    public static void main(String[] options) {

        try {

            long startTime = System.currentTimeMillis();
            ArffCreator creator = new ArffCreator();
            String[] classList = null;
            try {
                classList = creator.classList(options);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            if (classList != null) {
                String[] proteinList = creator.getProteinsForClassList(classList);
                String[] allMotifs = creator.getDistinctMotifs();

                System.out.println("Number of Classes: " + classList.length);
                System.out.println("Number of Motifs: " + allMotifs.length);
                System.out.println("Number of Proteins: " + proteinList.length);

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
                    FileOutputStream fout = new FileOutputStream(creator.filename);
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
                        System.out.println("Currently in " + (i + 1) + " from " + proteinList.length);
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
                    }
                } catch (IOException exp) {
                    System.out.println("Error: " + exp);
                    exp.printStackTrace();
                }

                System.out.println("ArffCreator" + Total.showTime(System.currentTimeMillis() - startTime));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
