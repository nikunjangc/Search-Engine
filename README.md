# Search-Engine
Cloning of how google search works under the hood.



About the Project

1.I have completed all the required specifications for the project. Program runs through the command line arguments

2.You can run from the command line.

3. Give the directory name and give the associated files name in the directory.

Every phase can be complied. Use the command line to run the program. Below mentions are the arguments used in the program

-CorpusDir

-InvertedIndex

-stemming

-StopList

-Queries

-Results

Command line argument that I have used is:

-CorpusDir /Users/nikunjan/Desktop/SearchEngineIR/corpus -InvertedIndex Inverted_Index_New.txt -StopList stop_word.txt -Queries queries.txt -Results result.txt -stemmingFlag y -GuiFlag yes

1.Firstly, once you input the arguments -CorpusDir, it imports all the files from the corpus folder and saves the file location on "corpusfile.txt"

2.If you pass the arguments -stemming, yes it calls the porterStemmer.java file and stemmed the inverted index file, if ypu pass -stemming, no in the argument it doesnot calls the stemming function

3.if you pass the arguments -StopList, it filters the common word from the corpus data

4.if you pass the arguments -InvertedIndex, it creates the inverted index and saves the inverted index file on "Inverted_Index_New.txt".

5.once you make the inverted index I have passed the persistence check checkiteration.txt which holds 0 and when you run for the first time it will

run from the beginning and once you find the inverted index it will now change the persistence check checkiteration.txt which holds 1 and now

every time you run the program it will reuse the saved file index ,stemming file, inverted index file data

7.if you pass the arguments -Queries , "your query", your query will be processed.

8.if you pass the arguments -Results , "it will display the snippet in a GUI , of your queries from top documents".
