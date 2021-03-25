import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.*;
import java.util.*;

public class kuir {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, ClassNotFoundException {

        String argument1 = args[0];
        String argument2 = args[1];
        ArrayList<Item> itemList = new ArrayList<>();
        ArrayList<Item> itemListKkma = new ArrayList<>();

        makeCollection mc = new makeCollection();
        makeKeyword mk = new makeKeyword();
        indexer mi = new indexer();
        System.out.println(argument1 + " " + argument2);

        //2주차
        //-c htmlSet
        if(argument1.equals("-c")){
            File path = new File("htmlSet");
            mc.mcCollection(itemList, path);
        }

        //3주차 : collection.xml 읽고 body에 들어가 있는 애 형태소 분석해서 다른 item으로 만들어서 WriteXml 한 번 더 돌리기
        //-k src/xmlSet/collection.xml
        else if(argument1.equals("-k")){
            File path = new File(argument2);
            mk.mkKeyword(itemListKkma, path, mc);
        }

        //4주차
        //-i src/xmlSet/index.xml
        else if(argument1.equals("-i")){
            File path = new File(argument2);
            HashMap<String, ArrayList<String>> result = mi.readFile(path);
            mi.writeInvertedFile(result);
        }
    }
}

    class Item {
        String name;
        String body;

        Item(String name, String body){
            this.name = name;
            this.body = body;
        }
    }
