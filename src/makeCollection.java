import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class makeCollection {
    public static void mcCollection(ArrayList<Item> itemList, File path) throws IOException, ParserConfigurationException, TransformerException {
        File[] fileList = path.listFiles(); //File name list //htmlSet 에 존재하는 모든 파일들을 fileList에 넣음

        if(fileList.length >0) {
            for(int i=0;i<fileList.length;i++) {
                //System.out.println("File "+fileList[i]);
                addList(fileList[i], itemList);
            }
        }
        WriteXml(itemList,"src/xmlSet/collection.xml");
    }

    public static void WriteXml(ArrayList<Item> itemList, String address) throws ParserConfigurationException, FileNotFoundException, TransformerException {
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
            System.out.println(index+" "+item.name);
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

    //Jsoup 이용해서 item에 저장
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
