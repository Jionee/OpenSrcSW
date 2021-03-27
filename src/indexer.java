import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class indexer {
    public static void writeInvertedFile(HashMap<String, ArrayList<String>> result) throws IOException, ClassNotFoundException {
        FileOutputStream fileStream = new FileOutputStream("src/xmlSet/index.post");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

        HashMap tmpHash = new HashMap();
        ArrayList<String> tmpValue = new ArrayList<>();

        for(String key: result.keySet()){
            ArrayList<String> valueList = result.get(key);
            for(String value:valueList){
                //tmpHash.put(key,value);
                tmpValue = new ArrayList<>();

                if(tmpHash.containsKey(key)){
                    ArrayList<String> tmp = (ArrayList<String>) tmpHash.get(key);
                    for(String string:tmp){
                        tmpValue.add(string);
                    }
                    String[] tmpSet = value.split(":");
                    tmpValue.add(tmpSet[0]);
                    tmpValue.add(tmpSet[1]);
                    tmpHash.put(key,tmpValue);
                }
                else{
                    String[] tmpSet = value.split(":");
                    tmpValue.add(tmpSet[0]);
                    tmpValue.add(tmpSet[1]);
                    tmpHash.put(key,tmpValue);
                }
            }
        }
        objectOutputStream.writeObject(tmpHash);
        objectOutputStream.close();

        //====확인====
        FileInputStream fileInputStream = new FileInputStream("src/xmlSet/index.post");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;
        Iterator<String> it = hashMap.keySet().iterator();

        while(it.hasNext()){
            String key = it.next();
            ArrayList<String> value = (ArrayList<String>) hashMap.get(key);
            System.out.println(key + " -> "+ value);
        }
    }

    public static HashMap<String, ArrayList<String>> readFile(File file) throws IOException {
        //Read file by String
        File input = new File(file.getPath());
        Document document = Jsoup.parse(input, "UTF-8", "http://example.com/");

        Elements docs = document.getElementsByTag("doc");
        //System.out.println("docs --> "+docs);
        HashMap<String, ArrayList<String>> result = getResultHashMap(docs);
        //System.out.println("RESULT ==> "+result);
        return result;

    }

    public static HashMap<String, ArrayList<String>> getResultHashMap(Elements docs) {
        //doc마다 돌아가면서 body 떼서 아이템에 저장하기
        ArrayList<ArrayList<HashMap<String,String>>> finalList = new ArrayList<>();
        HashMap<String,Integer> frequency = new HashMap<>();

        for(Element item: docs){
            ArrayList<HashMap<String,String>> itemFrequencyList = new ArrayList<>(); //한 도큐먼트의 리스트들 저장

            String body = item.text().split(" ")[1]; //body 태그가 사라져 있어서 띄어쓰기 기준으로 split 후 body내용 가져오기
            //System.out.println(body);
            //body 안에 존재하는것들 #기준으로 떼서 HashMap에 저장 [떡:17, 멥쌀:4, 찹쌀:3, 곡식:1, 가루:4, 음식:1, 통칭:1, 말:3, 일반적:1, 을:3, 주재료:1, ... ]
            List<String> list = Arrays.asList(body.split("#"));
            //System.out.println(list);
            HashMap<String,String> itemFrequency = new HashMap<>();
            for(String string : list){
                String key = string.split(":")[0];
                itemFrequency.put(key,string.split(":")[1]);

                //System.out.println("==="+key);
                //해당 단어가 몇 개의 문서에 있는지 frequency에 저장
                if(frequency.containsKey(key)){ //존재하면
                    int keyNum = frequency.get(string.split(":")[0]);
                    frequency.put(key,++keyNum);
                }
                else{
                    frequency.put(key,1);
                }
            }
            itemFrequencyList.add(itemFrequency);
            finalList.add(itemFrequencyList);
            //System.out.println("ItemFrequencyList --> "+itemFrequencyList);
        }
        //System.out.println(finalList);
        //System.out.println("FREQUENCY"+frequency);

        HashMap<String, ArrayList<String>> result = calculateTF_IDF(finalList, frequency);
        return result;
    }

    public static HashMap<String, ArrayList<String>> calculateTF_IDF(ArrayList<ArrayList<HashMap<String, String>>> finalList, HashMap<String, Integer> frequency) {
        //계산하기(해당 문서에서 몇 번 등장했는지)
        HashMap<String,ArrayList<String>> result = new HashMap<>(); //결과Map
        int index = 0; //몇 번째 문서
        for(ArrayList<HashMap<String,String>> itemList : finalList){
            for(HashMap<String,String> item : itemList){
                for(String key : item.keySet()){
                    int number = 0;
                    //몇 개의 문서에 존재하는지
                    double cal = Double.parseDouble(item.get(key)) * Math.log( 5 / (double) frequency.get(key));
                    cal = Math.round(cal*100)/100.0;
                    String first = key;
                    String second = index + ":" + Double.toString(cal);
                    //System.out.println(" ==> "+key+" 해당빈도수 : "+Double.parseDouble(item.get(key))+" 몇개문서 : "+(double)frequency.get(key)+ " cal : "+cal);

                    ArrayList<String> newStringList = new ArrayList<>();
                    if(result.containsKey(key)) {
                        newStringList = result.get(first);
                    }
                    newStringList.add(second);
                    result.put(first,newStringList);
                   /* if(result.containsKey(key)){
                        ArrayList<String> newStringList = new ArrayList<>();
                        newStringList = result.get(first);
                        newStringList.add(second);
                        result.put(first,newStringList);
                    }
                    else{
                        ArrayList<String> newStringList = new ArrayList<>();
                        newStringList.add(second);
                        result.put(first,newStringList);
                    }*/
                }
            }
            index++;
        }
        return result;
    }

}
