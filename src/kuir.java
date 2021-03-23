
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class kuir {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, ClassNotFoundException {

        String argument = args[0];
        ArrayList<Item> itemList = new ArrayList<>();
        ArrayList<Item> itemListKkma = new ArrayList<>();

        makeCollection mc = new makeCollection();
        makeKeyword mk = new makeKeyword();
        makeInvertedFile mi = new makeInvertedFile();
        System.out.println(argument);

        //2주차
        if(argument.equals("htmlSet")){
            File path = new File("htmlSet");
            mc.mcCollection(itemList, path);
        }

        //3주차 : collection.xml 읽고 body에 들어가 있는 애 형태소 분석해서 다른 item으로 만들어서 WriteXml 한 번 더 돌리기
        else if(argument.equals("src/xmlSet/collection.xml")){
            File path = new File(argument);
            mk.mkKeyword(itemListKkma, path, mc);
        }

        //3주차
        else if(argument.equals("src/xmlSet/index.xml")){
            File path2 = new File("src/xmlSet/collection.xml");
            mk.mkKeyword(itemListKkma, path2, mc);

            File path = new File(argument);
            HashMap<String, ArrayList<String>> result = mi.readFile(path);
            mi.writeInvertedFile(argument, result);
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
