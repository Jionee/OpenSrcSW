import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class kuir {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, ClassNotFoundException {

        String argument1 = null,argument2 = null,argument3 = null,argument4 = null;
        argument1 = args[0];
        argument2 = args[1];

        ArrayList<Item> itemList = new ArrayList<>();
        ArrayList<Item> itemListKkma = new ArrayList<>();

        makeCollection mc = new makeCollection();
        makeKeyword mk = new makeKeyword();
        indexer mi = new indexer();
        searcher sc = new searcher();
        System.out.println(argument1 + " " + argument2);


        // 중간고사
        genSnippet gs = new genSnippet();

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

        //5주차
        //-s src/xmlSet/index.post -q "라면에는 면,분말 스프가 있다."
        else if(argument1.equals("-s")){
            argument3 = args[2];
            if(argument3.equals("-q")){
                argument4 = args[3];
            }
            File path = new File(argument2);
            String query = argument4;
            System.out.println(argument1+argument2+argument3+argument4);
            sc.CalcSim(path,query);
        }

        //중간고사
        //-f src/input.txt -q "라면 반죽 소금 초밥 채소"
        else if(argument1.equals("-f")){
            //input.txt를 읽어서 입력한 키워드가 가장 많이 포함된 라인을 출력한다.
            //넣어주는 스트링
            argument3 = args[2];
            if(argument3.equals("-q")){
                argument4 = args[3];
            }
            gs.gitSnippetFunction(argument2, argument4);

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
