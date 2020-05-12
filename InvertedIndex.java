import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class InvertedIndex {


    //file id of each corpus
    static Map<Integer, String> fileFrom;

    //file doc -index
    static HashMap<String, Set<Integer>> index;

    static  String titles="";
    // term frequency for each individual document of the corpus
    static Map<Integer, Map<String, Integer>> termFreqMap;

    Hashtable<Integer, Integer> documentTerms = new Hashtable<>();

    //storing number of terms for each document. This is later used to calculate tf-idf.
    static Map<Integer, Double> docTerms = new HashMap<>();

    public static int corpusDocNum = 1;  //This is used to store each word file number

    //porterStemmer
    public static PorterStemmer porterStemmer = new PorterStemmer();

    InvertedIndex() {
        fileFrom = new HashMap<Integer, String>();
        index = new HashMap<String, Set<Integer>>();

    }

    public void indexHtmlFiles1(String[] files) throws FileNotFoundException {  //find the inverted index without stemming
        termFreqMap = new HashMap<Integer, Map<String, Integer>>();

        for (String file : files) {

            StringBuilder html = new StringBuilder();
            FileReader fr = new FileReader(file);
            try {


                BufferedReader br = new BufferedReader(fr);
                String val;
                while ((val = br.readLine()) != null) {
                    html.append(val);
                }
                br.close();
                String result1 = html.toString();
                //for finding the corpus
                String result = result1.replaceAll("<style[\\w\\W]+?</style>", "");
                String str = result.replaceAll("\\<[^>]*>", " ");  //remove tag
                String str2 = str.replaceAll("[^a-zA-Z]", "  "); //remove non-alphabet
                String str3 = str2.replaceAll("\\bwg\\w+", " "); //remove word start with wg
                String str4 = str3.replaceAll(" (\\b[A-z]\\b)", " "); //remove single letter
                String str5 = str4.replaceAll("\\bmw", " ");//remove mw
                String str6 = str5.replaceAll("\\<.*?\\>", " ");

                String stripped = str6.replaceAll("</?(font|p){1}.*?/?>", "");
                String str7 = stripped.replaceAll("\\p{Punct}", " ").toLowerCase();

                String[] arrOfStr = str7.split("\\s+");
                documentTerms.put(corpusDocNum, arrOfStr.length);


            } catch (IOException e) {
                e.printStackTrace();
            }
            //just for testing purpose
//            if (corpusDocNum == 5) {
//                break;
//            }


            corpusDocNum++; //count the number of the document id
        }


    }


    public void indexHtmlFiles(String[] files) throws FileNotFoundException { //find the inverted index with stemming
        termFreqMap = new HashMap<Integer, Map<String, Integer>>();

        for (String file : files) {

            StringBuilder html = new StringBuilder();
            FileReader fr = new FileReader(file);
            try {


                BufferedReader br = new BufferedReader(fr);
                String val;
                while ((val = br.readLine()) != null) {
                    html.append(val);
                }
                br.close();
                String result1 = html.toString();
                // System.out.println(result);
                String result = result1.replaceAll("<style[\\w\\W]+?</style>", "");
                String str = result.replaceAll("\\<[^>]*>", " ");  //remove tag
                String str2 = str.replaceAll("[^a-zA-Z]", "  "); //remove non-alphabet
                String str3 = str2.replaceAll("\\bwg\\w+", " "); //remove word start with wg
                String str4 = str3.replaceAll(" (\\b[A-z]\\b)", " "); //remove single letter
                String str5 = str4.replaceAll("\\bmw", " ");//remove mw
                String str6 = str5.replaceAll("\\<.*?\\>", " ");

                String stripped = str6.replaceAll("</?(font|p){1}.*?/?>", "");
                String str7 = stripped.replaceAll("\\p{Punct}", " ").toLowerCase();

                String[] arrOfStr = str7.split("\\s+");
                documentTerms.put(corpusDocNum, arrOfStr.length);
                if (SearchEngine.stemming.equalsIgnoreCase("y")) {  //if the flag is yes will call the stemming function
                    invertedIndexC(arrOfStr);
                } else {
                    invertedIndexWithoutStemming(arrOfStr);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            //just for testing purpose
//            if (corpusDocNum == 10) {
//                break;
//            }


            corpusDocNum++;
        }


    }

    static ArrayList stopwords = new ArrayList();
    static ArrayList queries = new ArrayList();
    static ArrayList dumbData = new ArrayList();


    public static ArrayList stopWordCreate() {
        String data;
        try {
            String path1 = SearchEngine.stopList1;
            // String path = System.getProperty("user.dir") + "/stop_word.txt/";
            String path = System.getProperty("user.dir") + "/" + SearchEngine.stopList1;
            System.out.println(path);

            File file = new File(path);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {

                data = myReader.nextLine();
                queries.add(data);

                //  System.out.println(data);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // = data.split("\\s+");
        // System.out.println("Stop list is" + queries.size());
        return queries;
    }


    public static boolean isStopWord(String str) {  //will call the stop word for the queries, to avoid eg:the, to ...

        if (arrayContains1(queries, str)) {
            return true;
        }

        return false;
    }


    public static boolean arrayContains1(ArrayList stopwords, String s) {

        for (int i = 0; i < stopwords.size(); i++) {
            if (stopwords.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public void printInvertedIndex() {  //find the inverted index
        String invertedIndexFile0 = SearchEngine.invertedIndex1;
        String invertedIndexFile01 = SearchEngine.query; //takes the user input from the file
        String invertedIndexFile = System.getProperty("user.dir") + "/" + invertedIndexFile0;
        String invertedIndexFile1 = System.getProperty("user.dir") + "/" + invertedIndexFile01;
//        String invertedIndexFile = System.getProperty("user.dir") + "/Inverted_Index_New.txt/";
//        String invertedIndexFile1 = System.getProperty("user.dir") + "/Inverted_Index_Search_Query.txt/";
        int count = 0;
        try {
            FileWriter fileWriter = new FileWriter(invertedIndexFile);
            FileWriter fileWriter1 = new FileWriter(invertedIndexFile1);

            for (Map.Entry<Integer, Map<String, Integer>> element : termFreqMap.entrySet()) {

                try {


                    //  System.out.print(element.getKey() + " -" + "->");  //for testing purpose
                    fileWriter.write((element.getKey() + "-"));

                    //Set<Integer> ids = index.get(element.getKey());
                    Map<String, Integer> hashinside = element.getValue();
                    int size = hashinside.size();

                    int i = 0;
                    for (Map.Entry e : hashinside.entrySet()) {
                        if (i == size - 1) {
                            fileWriter.write(e.getKey() + ":" + e.getValue());
                        } else {
                            fileWriter.write(e.getKey() + ":" + e.getValue() + ",");
                        }
                        i++;
                    }
                    fileWriter.write("\n");


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            fileWriter.close();
            // }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static String searchInInvertedIndex() throws IOException {
        String data;
        // String path = System.getProperty("user.dir") + "/queries.txt/";
        String path = System.getProperty("user.dir") + "/" + SearchEngine.query;

        String queryWord = "";


        File myObj1 = new File(path);
        String token = " ";
        Scanner myReader = new Scanner(myObj1);
StringBuilder multiQuery=new StringBuilder();
        while (myReader.hasNextLine()) {
            token = myReader.nextLine();
            multiQuery.append(token+" ");

        }
        titles+=multiQuery.toString();
        String[] Query = multiQuery.toString().toLowerCase().split(" ");



        for (int i = 0; i < Query.length; i++) {
            //System.out.println(stopListWord[3]);
            data = Query[i].replaceAll("\\p{Punct}", "");
            if (isStopWord(data)) {

                continue;
            }
            queryWord += data;
            queryWord += " ";


        }

        return queryWord;

    }




    public static boolean isInInvertedList(String str) {

        if (arrayContains(queries, str)) {
            return true;
        }

        return false;
    }


    public static boolean arrayContains(ArrayList stopwords, String s) {

        for (int i = 0; i < stopwords.size(); i++) {
            if (stopwords.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static Hashtable<Integer, Hashtable<String, Integer>> createHashfromtextfile(String Filepath) {
        Hashtable<Integer, Hashtable<String, Integer>> H2 = new Hashtable<>();
        Hashtable<String, Integer> H1;
        String data = "";
        try {
            File myObj = new File(Filepath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                // System.out.println(data);
                String[] s1 = data.split("-");
                String[] s2 = s1[1].split(",");
                H1 = new Hashtable<String, Integer>();
                for (int i = 0; i < s2.length; i++) {
                    String[] s3 = s2[i].split(":");
                    H1.put(s3[0], Integer.parseInt(s3[1]));
                }
                H2.put(Integer.parseInt(s1[0]), H1);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        // System.out.println(H2.toString());
        // System.out.println();
        return H2;
    }

    public void Search(String x1, Hashtable<Integer, Hashtable<String, Integer>> finalHash) throws IOException {
        //Hashtable<String, Integer> insideTable=new Hashtable<>();
        ArrayList<Integer> docID = new ArrayList();
        Map<Integer, Double> tfIdfTable = new HashMap<>();//stroing tf-idf for corresponding document id
        String[] x2 = x1.split(" ");
        double totaltfIdf = 0;

        stopWordCreate();
        String s="";//display

        for (String word : x2) {   //checks for common words which is not required in search
            String word1 = word.replaceAll(("\\p{Punct}"), "");
            // stop word check
            System.out.println(word1);
            if (isStopWord(word1)) {  //check for the stop word
                continue;
            } else {
                for (String a : x2) {

                    for (Map.Entry<Integer, Hashtable<String, Integer>> element : finalHash.entrySet()) {

                        Hashtable<String, Integer> insideTable = element.getValue();
                        //System.out.println(insideTable.toString());
                        if (insideTable.containsKey(a)) {   //checks if the word is in particular document
                            if (docID.contains(element.getKey())) {

                            } else {
                                docID.add(element.getKey());
                            }
                        }


                    }
                }
                for (String a : x2) {

                    for (Map.Entry<Integer, Hashtable<String, Integer>> element : finalHash.entrySet()) {

                        Hashtable<String, Integer> insideTable = element.getValue();
                        //System.out.println(insideTable.toString());
                        if (insideTable.containsKey(a)) {   //checks if the word is in particular document


                            int tf = insideTable.get(a);
                            int N = SearchEngine.getNumberofFiles();
                            double normalizedTF = Double.parseDouble(String.valueOf(tf)) / documentTerms.get(element.getKey());
                            double idf = Math.log(N / docID.size());

                            double tfIdf = normalizedTF * idf;
                            totaltfIdf += tfIdf;

                            Double x = tfIdfTable.get(element.getKey());

                            if (x == null) {
                                tfIdfTable.put(element.getKey(), tfIdf);
                            } else {
                                tfIdfTable.put(element.getKey(), x + tfIdf);
                            }


                        }


                    }
                    // tfIdfTable.put();
                }
                System.out.print("docID");
                System.out.println(docID);
                for (int d : docID
                ) {
                    //System.out.println(titleDocument(d));


                }
                //System.out.println(finalHash.toString());
            }
        }

        Map<Integer, Double> sortedTfidfTable = tfIdfTable.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

        System.out.println(sortedTfidfTable);

        double avergaetfIdf = ((totaltfIdf / tfIdfTable.size()));

        System.out.println("\n\nresults:");
        FileWriter myWriter = new FileWriter(System.getProperty("user.dir")+ "/"+SearchEngine.result1);
        for (Map.Entry<Integer, Double> entry : sortedTfidfTable.entrySet()) {

            //if tf-idf is less than average than break
            //this controls precision
            if (entry.getValue() < avergaetfIdf) break;
            //result.add(source.get(entry.getKey()));


             s += titleDocument(entry.getKey(), x2);

           // System.out.println(titleDocument(entry.getKey(), x2));


        }
        myWriter.write(s);
        myWriter.close();

    }


    public String titleDocument(int docId, String[] words) throws IOException {
        String result1 = "";
        String titleFinal = "";

        try {
            String[] files = SearchEngine.files();
            int countFile = 0;
            String s="";
            for (String fileName : files) {
                countFile++;
                if (countFile == docId) {
                    StringBuilder html = new StringBuilder();
                    FileReader fr = new FileReader(fileName);
                    try {


                        BufferedReader br = new BufferedReader(fr);
                        String val;
                        while ((val = br.readLine()) != null) {
                            html.append(val);
                        }
                        br.close();
                        result1 = html.toString();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Pattern p1 = Pattern.compile("<title>");
                    Matcher m1 = p1.matcher(result1);
                    Pattern p2 = Pattern.compile("</title>");

                    Matcher m2 = p2.matcher(result1);
                    int count = 0;
                    StringBuilder title = new StringBuilder();
                    while (m1.find() && m2.find()) {

                        count++;

                        for (int i = m1.end(); i < m2.start(); i++) {
                            title = title.append(result1.charAt(i));
                        }
                        String fTitle = title.toString();
                        //trim tile unnecessary tags and non character
                        if (fTitle.contains("&")) {
                            int a = 0;
                            for (int i = 0; i < fTitle.length(); i++) {
                                if (fTitle.charAt(i) == '&') {
                                    a = i;
                                    break;
                                }
                            }
                            fTitle = fTitle.substring(0, a);


                        }

                        titleFinal = "\nTitle: " + fTitle;


                    }
                    titleFinal += "\n\nSnippet: ";
                    String result2 = result1.replaceAll("<style[\\w\\W]+?</style>", "");
                    String str = result2.replaceAll("\\<[^>]*>", " ");  //remove tag
                    String str2 = str.replaceAll("[^a-zA-Z]", "  "); //remove non-alphabet
                    String str3 = str2.replaceAll("\\bwg\\w+", " "); //remove word start with wg


                    String str10 = str3.replaceAll("https", " ");
                    String str11 = str10.replaceAll("www", " ");
                    String str12 = str11.replaceAll("com", " ");


                    String str7 = str12.replaceAll("\\bmw", " ");//remove mw
                    String str99 = str7.replaceAll("\\s++", " ");

                    String str8 = str99.replaceAll("\\p{Punct}", " ");
                    String str5 = str8.toLowerCase();

                    for (String word : words) {
                        titleFinal += Snippetreturn(word, str5);
                        titleFinal += "... ";
                    }

                    titleFinal += "\n\n\n";


                }

            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        return titleFinal;
    }

    public static String Snippetreturn(String s1, String temp) {

        StringBuilder word4 = new StringBuilder();
        String snippet = " ";

        Pattern p = Pattern.compile(s1.toLowerCase());   // the pattern to search for
        Matcher m1 = p.matcher(temp);


        int count = 0;


        while (m1.find()) {
            //System.out.println("found: " + m1.find()) ;
//                                   // + m1.start() + " - " + m1.end());
//
            int y = 0;
            int x = 0;
            char fullStopMatch = temp.charAt(m1.start() - x);
            int length = temp.length();

            while (fullStopMatch != '.') {


                if (x > m1.start()) {


                    x = m1.start();
                    if (x > 50) {
                        x = 50;
                        if ((temp.charAt(m1.start() - x + 1) != ' ')) {
                            while ((temp.charAt(m1.start() - x) != ' ')) {
                                x++;
                            }
                        }

                    }
                    break;

                } else {
                    fullStopMatch = temp.charAt(m1.start() - x);

                    x++;
                }

            }


            for (int i = m1.start()-x ; i < m1.end() +40; i++) {

                word4 = word4.append(temp.charAt(i));


            }
            snippet = word4.toString();
            count++;
            if (count == 1) break;

        }
        //System.out.println("<-----------------------------------???????????---->");
        return snippet;
    }


    public static void invertedIndexC(String[] arrOfStr) {

        for (String word : arrOfStr) {

            //stop word check
            if (isStopWord(word)) {  //check for the stop word
                continue;
            }
            word = word.toLowerCase();
            word = porterStemmer.stem(word);
            //
            //indexing each term
            Set<Integer> doc_id = index.get(word);
            if (doc_id == null) {
                doc_id = new HashSet<Integer>();
                index.put(word, doc_id);
            }
            doc_id.add(corpusDocNum);
            //  System.out.println(word + " - " + corpusDocNum);


            //calculating term frequency
            Map<String, Integer> table = termFreqMap.get(corpusDocNum);
            Integer n = null;
            if (table != null) {
                n = table.get(word);
            } else {
                table = new HashMap<String, Integer>();
            }
            if (n == null) {
                n = 1;
            } else {
                n++;
            }

            table.put(word, n);
            termFreqMap.put(corpusDocNum, table);


        }


    }

    public static void invertedIndexWithoutStemming(String[] arrOfStr) {

        for (String word : arrOfStr) {

            //stop word check
            if (isStopWord(word)) {  //check for the stop word
                continue;
            }
            word = word.toLowerCase();
            //
            //indexing each term
            Set<Integer> doc_id = index.get(word);
            if (doc_id == null) {
                doc_id = new HashSet<Integer>();
                index.put(word, doc_id);
            }
            doc_id.add(corpusDocNum);
            //  System.out.println(word + " - " + corpusDocNum);


            //calculating term frequency
            Map<String, Integer> table = termFreqMap.get(corpusDocNum);
            Integer n = null;
            if (table != null) {
                n = table.get(word);
            } else {
                table = new HashMap<String, Integer>();
            }
            if (n == null) {
                n = 1;
            } else {
                n++;
            }

            table.put(word, n);
            termFreqMap.put(corpusDocNum, table);


        }
    }

}







