import java.io.*;
import java.util.Hashtable;
import java.util.Scanner;

public class SearchEngine {
    private static int numOfFiles = 0;

    static String result1 = "";
    static String corpusDir1 = "";
    static String stemming = "";
    static String stopList1 = "";
    static String invertedIndex1 = "";
    static String query = "";
    static String gui = "";

    public static void main(String[] args) throws IOException {


        CommandLine(args);

//        if (args[0].equals("one")) {

        InvertedIndex invertedIndex = new InvertedIndex();
        File myObj1 = new File(System.getProperty("user.dir") + "/checkIteration.txt/");
        //  File myObj1 = new File(System.getProperty("user.dir") + "/"+"checkIteration.txt"+"/");

        FileWriter myWriter1;
        String logno = "";
        Scanner myReader1 = new Scanner(myObj1);
        while (myReader1.hasNextLine()) {

            logno = myReader1.nextLine();
            logno = logno.trim();
            // System.out.println(data);
        }

        Hashtable<Integer, Hashtable<String, Integer>> finalHash;
        String[] files = files();
        String inverStore = "";
        if (logno.equals("0")) {


            myWriter1 = new FileWriter(myObj1);
            myWriter1.flush();

            myWriter1.write("1");

            myWriter1.close();

            ////


            int i = 0, j;
            String arg;
            char flag;
            boolean vflag = false;
            String outputfile = "";
            //  String path = System.getProperty("user.dir") + "/queries.txt/";
            String path = System.getProperty("user.dir") + "/" + query;

            while (i < args.length && args[i].startsWith("-")) {
                arg = args[i++];


            }

            //corpus dir


            invertedIndex.stopWordCreate();
            invertedIndex.indexHtmlFiles(files);
            inverStore = invertedIndex.searchInInvertedIndex();
            invertedIndex.printInvertedIndex();
            String pathtoInvertedIndex = System.getProperty("user.dir") + "/Inverted_Index_New.txt/";

            finalHash = invertedIndex.createHashfromtextfile(pathtoInvertedIndex);


        } else {
            invertedIndex.stopWordCreate();

            invertedIndex.indexHtmlFiles1(files);
            inverStore = invertedIndex.searchInInvertedIndex();
            //invertedIndex.searchInInvertedIndex();

            finalHash = InvertedIndex.createHashfromtextfile(System.getProperty("user.dir") + "/Inverted_Index_New.txt/"); //use
            // System.out.println(finalHash.toString());
        }
        System.out.println(inverStore);

        invertedIndex.Search(inverStore, finalHash);
        // invertedIndex.indexHtmlFiles(listOffiles);
        // write your code here
       // System.out.println("The no of word is" + finalHash.get(1).values().size());

        if (gui.equalsIgnoreCase("Yes")) {

            GuiDisplay screen = new GuiDisplay();
            screen.setVisible(true);
            screen.setSize(1000, 1500);
            screen.setLocation(200, 200);
        }
    }
    //}

    public static void CommandLine(String[] args) {

        int k = 0;
        //-CorpusDir /Users/nikunjan/Desktop/SearchEngineIR/corpus -InvertedIndex Inverted_Index_New.txt -StopList stop_word.txt -Queries queries.txt -Results x -Stemming ABC
        while (k < args.length) {
            String argument = args[k];
            if (argument.equals("-CorpusDir")) {
                if (k < args.length) {

                    int j = k;
                    j++;
                    if (args[j].equals("-InvertedIndex")
                            ||
                            args[j].equals("-stemmingFlag")
                            ||
                            args[j].equals("-StopList")
                            ||
                            args[j].equals("-GuiFlag")
                            ||
                            args[j].equals("-Queries")
                            ||
                            args[j].equals("-Results")) {

                        System.err.println("-output requires a CorpusDir");
                        System.exit(0);
                    } else {
                        corpusDir1 = args[++k];
//                        System.out.println("CorpusDir = " + corpusDir1);
//
                    }

                }

            } else if (argument.equals("-InvertedIndex")) {
                if (k < args.length) {
                    int j = k;
                    j++;
                    if (args[j].equals("-CorpusDir")
                            ||
                            args[j].equals("-stemmingFlag")
                            ||
                            args[j].equals("-StopList")
                            ||
                            args[j].equals("-GuiFlag")
                            ||
                            args[j].equals("-Queries")
                            ||
                            args[j].equals("-Results")) {

                        System.err.println("-Program requires a InvertedIndex FileName");
                        System.exit(0);
                    } else {
                        invertedIndex1 = args[++k];
                        System.out.println("InvertedIndex= " + invertedIndex1);
                    }


                }

            } else if (argument.equals("-stemmingFlag")) {
                if (k < args.length) {

                    int j = k;
                    j++;
                    if (args[j].equals("-CorpusDir")
                            ||
                            args[j].equals("-InvertedIndex")
                            ||
                            args[j].equals("-Queries")
                            ||
                            args[j].equals("-GuiFlag")
                            ||
                            args[j].equals("-StopList")
                            ||
                            args[j].equals("-Results")) {

                        System.err.println("-Program requires a StopList FileName");
                        System.exit(0);
                    } else {
                        stemming = args[++k];
                        System.out.println("StopList=" + stemming);
                    }
                }
            } else if (argument.equals("-StopList")) {
                if (k < args.length) {

                    int j = k;
                    j++;
                    if (args[j].equals("-CorpusDir")
                            ||
                            args[j].equals("-stemmingFlag")
                            ||
                            args[j].equals("-InvertedIndex")
                            ||
                            args[j].equals("-GuiFlag")
                            ||
                            args[j].equals("-Queries")
                            ||
                            args[j].equals("-Results")) {

                        System.err.println("-Program requires a StopList FileName");
                        System.exit(0);
                    } else {
                        stopList1 = args[++k];
                        System.out.println("StopList=" + stopList1);
                    }


                }

            } else if (argument.equals("-Queries")) {
                if (k < args.length) {
                    int j = k;
                    j++;
                    if (args[j].equals("-CorpusDir")
                            ||
                            args[j].equals("-stemmingFlag")
                            ||
                            args[j].equals("-InvertedIndex")
                            ||
                            args[j].equals("-GuiFlag")
                            ||
                            args[j].equals("-StopList")
                            ||
                            args[j].equals("-Results")) {

                        System.err.println("-Program requires a Queries FileName");
                        System.exit(0);
                    } else {
                        query = args[++k];
                        System.out.println("Queries =" + query);
                    }

                }

            } else if (argument.equals("-Results")) {
                if (k < args.length) {
                    int j = k;
                    j++;
                    if (args[j].equals("-CorpusDir")
                            ||
                            args[j].equals("-stemmingFlag")
                            ||
                            args[j].equals("-InvertedIndex")
                            ||
                            args[j].equals("-GuiFlag")
                            ||
                            args[j].equals("-StopList")
                            ||
                            args[j].equals("-Queries")) {

                        System.err.println("-Program requires a Results FileName");
                        System.exit(0);
                    } else {
                        result1 = args[++k];
                        System.out.println(result1);
                    }
                }

            } else if (argument.equals("-GuiFlag")) {
                if (k < args.length) {
                    int j = k;
                    j++;
                    if (args[j].equals("-CorpusDir")
                            ||
                            args[j].equals("-stemmingFlag")
                            ||
                            args[j].equals("-InvertedIndex")
                            ||
                            args[j].equals("-Results")
                            ||
                            args[j].equals("-StopList")
                            ||
                            args[j].equals("-Queries")) {

                        System.err.println("-Program requires a Results FileName");
                        System.exit(0);
                    } else {
                        gui = args[++k];
                        System.out.println(result1);
                    }
                }

            }
            k++;
        }

    }


    public static String[] files() throws FileNotFoundException {

        //String path = "C:/Search Engine Project/corpus/";
        String path1 = corpusDir1;
        //String path = System.getProperty("user.dir") + "/corpus/";
        String path = corpusDir1;


        File dir = new File(path);

        File[] file_list = dir.listFiles();
        if (file_list != null) {
            int i = 0;
            for (File child : file_list) {
                // htmlSeparate(child);
                //System.out.println(child);
                numOfFiles++;
            }
        }
        String files[] = new String[(int) numOfFiles];
        if (file_list != null) {
            int i = 0;
            for (File child : file_list) {
                //  System.out.println(child);
                files[i] = child.toString();
                i++;
            }
        }

        return files;
    }

    public static int getNumberofFiles() {
        return numOfFiles;
    }


}



