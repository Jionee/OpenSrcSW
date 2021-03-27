import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class makeKeyword {
    public static void mkKeyword(ArrayList<Item> itemListKkma, File collectionXml,makeCollection mc) throws IOException, ParserConfigurationException, TransformerException {
        ArrayList<Item> originalItemList = new ArrayList<>();

        readFile(collectionXml,originalItemList);

        for(int i=0;i<originalItemList.size();i++){
            String newBody = kkmaString(originalItemList, i);
            itemListKkma.add(new Item(originalItemList.get(i).name,newBody));
        }

        mc.WriteXml(itemListKkma, "src/xmlSet/index.xml");
    }

    public static String kkmaString(ArrayList<Item> itemList, int i) {
        String body = itemList.get(i).body;
        System.out.println(i+" ==>"+body);
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(body,true); //extract keyword
        String newBody = "";
        for (int k=0;k<kl.size();k++){
            Keyword kwrd = kl.get(k);
            if(kwrd.getString().equals(itemList.get(i).name)){
                newBody+=kwrd.getString() + ":" + (kwrd.getCnt()-1) + "#";
            }
            else{
                newBody+=kwrd.getString() + ":" + kwrd.getCnt() + "#";
            }
        }
        //System.out.println(newBody);
        return newBody;
    }

    public static void readFile(File file, ArrayList<Item> itemList) throws IOException {
        //Read file by String
        File input = new File(file.getPath());
        Document document = Jsoup.parse(input, "UTF-8", "http://example.com/");
        Elements docs = document.getElementsByTag("doc");
        //System.out.println("docs --> "+docs);
        //doc마다 돌아가면서 title, body 떼서 아이템에 저장하기
        for(Element item:docs){
            String name = item.select("title").text();
            String body = item.text();

            //System.out.println("name --> "+name); System.out.println("body --> "+body);
            itemList.add(new Item(name,body)); //store by Object
        }
    }
}
