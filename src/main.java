
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
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException ,ParserConfigurationException, TransformerException{

        File path = new File("htmlSet");
        File[] fileList = path.listFiles(); //File name list

        ArrayList<Item> itemList = new ArrayList<>();
        ArrayList<Item> itemListKkma = new ArrayList<>();

        if(fileList.length >0) {
            for(int i=1;i<fileList.length;i++) {
                if(fileList[i].equals("htmlSet\\collention.xml")) continue;
                addList(fileList[i], itemList);
            }
        }
        WriteXml(itemList,"htmlSet/collection.xml");

        //3주차 : body에 들어가 있는 애 형태소 분석해서 다른 item으로 만들어서 WriteXml 한 번 더 돌리기
        for(int i=1;i<itemList.size();i++){
            String newBody = kkmaString(itemList, i);
            itemListKkma.add(new Item(itemList.get(i).name,newBody));
        }
        WriteXml(itemListKkma, "htmlSet/index.xml");
    }

    private static String kkmaString(ArrayList<Item> itemList, int i) {
        String body = itemList.get(i).body;
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(body,true); //extract keyword
        String newBody = "";
        for (int k=0;k<kl.size();k++){
            Keyword kwrd = kl.get(k);
            newBody+=kwrd.getString() + ":" + kwrd.getCnt() + "#";
        }
        //System.out.println(newBody);
        return newBody;
    }

    private static void WriteXml(ArrayList<Item> itemList,String address) throws ParserConfigurationException, FileNotFoundException, TransformerException {
        //make Collect Xml
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        org.w3c.dom.Document doc =  docBuilder.newDocument();

        //Complete Elements
        org.w3c.dom.Element elementDocs = doc.createElement("docs");
        doc.appendChild(elementDocs);

        //list만큼 for문 돌리기
        int index=0;
        for(Item item: itemList){
            //System.out.println(index+" "+item.name);
            org.w3c.dom.Element elementDoc = doc.createElement("doc");
            org.w3c.dom.Element elementTitle = doc.createElement("title");
            org.w3c.dom.Element elementBody = doc.createElement("body");

            elementDocs.appendChild(elementDoc);
            elementDoc.setAttribute("id", String.valueOf(index++));

            elementDoc.appendChild(elementTitle);
            elementTitle.appendChild(doc.createTextNode(item.name));

            elementDoc.appendChild(elementBody);
            elementBody.appendChild(doc.createTextNode(item.body));
        }

        //Write Xml File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");

        DOMSource source = new DOMSource((Node) doc);
        StreamResult result = new StreamResult(new FileOutputStream(new File( address )));

        transformer.transform(source,result);
    }

    public static void addList(File file, ArrayList<Item> itemList) throws IOException {
        //Read file by String
        File input = new File(file.getPath());
        Document document = Jsoup.parse(input, "UTF-8", "http://example.com/");
        Element item = document.getAllElements().get(0);
        //System.out.println("item --> "+item);

        String name = item.select("title").text();
        String body = item.select("#content").text();

        /*System.out.println("name --> "+name); System.out.println("body --> "+body);*/

        itemList.add(new Item(name,body)); //store by Object
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
