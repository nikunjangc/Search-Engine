import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GuiDisplay extends JFrame{

    JTextArea textArea = new JTextArea ();

    JTextArea textArea1= new JTextArea();


    public GuiDisplay() throws IOException {
        setTitle("Search Results");
        JPanel flowPanel = new JPanel (new FlowLayout (FlowLayout.CENTER));
        add (textArea,BorderLayout.CENTER);
        textArea.setEditable(false);


        add (flowPanel, BorderLayout.SOUTH);
        readFile (System.getProperty("user.dir") +"/"+SearchEngine.result1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(200, 300));
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll);

    }
    private void readFile(String fileName) throws IOException {

        String result ="";

        try{
            FileReader fileReader = new FileReader(fileName);
            Scanner sc = new Scanner(fileReader);
            while(sc.hasNext()==true){
                result = result+sc.nextLine()+"\n ";
            }
            sc.close();
            fileReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        textArea.setText("Searching for :"+(InvertedIndex.titles)+"\n"+result);
    }



    }



