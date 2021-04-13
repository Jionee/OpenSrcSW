import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class searcher {
    public void CalcSim(File path, String query) throws IOException, ClassNotFoundException {
        //쿼리 kkmastring 분석, index.post ==> 각 문서별 유사도 계산
        HashMap<String, Double> queryMap = new HashMap<>(); //kkma 분석
        HashMap hashMap = getQueryMap_hashMap(path, query, queryMap);

        ArrayList<Double> resultInnerProduct = getInnerProduct(queryMap, hashMap); //feature
        ArrayList<Double> resultCalc = getResultCalc(queryMap, hashMap,resultInnerProduct); //master
        //System.out.println(queryMap);
        //System.out.println(hashMap);

        //title가져오기
        File path2 = new File("src/xmlSet/index.xml");
        ArrayList<String> titleList = readTitle(path2);

        //sorting
        for(int i=0;i<resultCalc.size();i++){
            int tmp = i;
            for(int j=i+1;j<resultCalc.size();j++){
                if(resultCalc.get(tmp) < resultCalc.get(j)) tmp = j;
            }
            Collections.swap(resultCalc,i,tmp);
            Collections.swap(titleList,i,tmp);
        }
        //System.out.println(">> "+resultCalc);
        //System.out.println(">> "+titleList);

        //결과 출력
        for(int i=0;i<3;i++){
            System.out.println("Rank"+(i+1)+" : "+titleList.get(i));
        }
    }
    private ArrayList<Double> getInnerProduct(HashMap<String, Double> queryMap, HashMap hashMap){

        HashMap<String, Double> calResult = new HashMap<>();

        //내적
        ArrayList<Double> resultInnerProduct = new ArrayList<>();
        for(int i=0;i<5;i++){ //5개 문서에 대해서 유사도 내적 계산 (3번째 자리에서 반올림한 값으로)
            Double result0 = 0.0;
            Double val = 0.0;
            for(String key:queryMap.keySet()){
                ArrayList<String> value = (ArrayList<String>) hashMap.get(key);
                //System.out.println("~~~~ "+value);
                for(int k=0;k<value.size();){
                    if(Integer.parseInt(value.get(k))==i){
                        val = Double.parseDouble(value.get(k+1));
                        //System.out.println(i+" && "+val);
                        break;
                    }
                    k+=2;
                }
                result0 += queryMap.get(key) * val;
            }
            resultInnerProduct.add(Math.round(result0*100)/100.0);
        }
        //System.out.println(resultCalc);
        return resultInnerProduct;
    }

    private ArrayList<Double> getResultCalc(HashMap<String, Double> queryMap, HashMap hashMap, ArrayList<Double> resultInnerProduct){

        //내적 --> Cosine
        ArrayList<Double> resultCalc = new ArrayList<>();
        Double powKkma = 0.0;//query제곱
        Double sqrtPowKkma = 0.0;
        Double sqrt = 0.0;
        for(String key:queryMap.keySet()){
            powKkma += Math.pow(queryMap.get(key), 2);
        }
        sqrtPowKkma = Math.sqrt(powKkma);
        for(int i=0;i<5;i++){ //5개 문서에 대해서 Cosine 계산 (3번째 자리에서 반올림한 값으로)
            Double result0 = 0.0;
            Double powPost = 0.0;//
            Double val = 0.0;
            for(String key:queryMap.keySet()){ //Query를 kkmaString 분석한 것
                ArrayList<String> value = (ArrayList<String>) hashMap.get(key);
                for(int k=0;k<value.size();) {
                    if (Integer.parseInt(value.get(k)) == i) {
                        //루트 wq1제곱 + wq2제곱 + ..
                        val = Double.parseDouble(value.get(k + 1));
                        powPost += Math.pow(val,2);//
                        //System.out.println(i+" && "+val);
                        break;
                    }
                    k += 2;
                }
            }
            Double sqrtPowPost = Math.sqrt(powPost);//
            sqrt = sqrtPowKkma * sqrtPowPost;
            result0 = resultInnerProduct.get(i) / (sqrt);
            
            resultCalc.add(Math.round(result0*100)/100.0);
        }
        System.out.println("RESULTCALC : "+ resultCalc);

        return resultCalc;
    }

    private HashMap getQueryMap_hashMap(File path, String query, HashMap<String, Double> queryMap) throws IOException, ClassNotFoundException {
        //kkmastring 분석
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(query,true); //extract keyword
        //System.out.println("extract ==> " + query);
        //HashMap<String, Double> queryMap = new HashMap<>();

        for(int i=0;i<kl.size();i++){
            Keyword kwrd = kl.get(i);
            queryMap.put(kwrd.getString(),1.0);
            //System.out.println(kwrd.getString()+"\t"+kwrd.getCnt());
        }

        //index.post 가져오기
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;
        return hashMap;
    }

    public static ArrayList<String> readTitle(File file) throws IOException {
        //Read file by String
        File input = new File(file.getPath());
        Document document = Jsoup.parse(input, "UTF-8", "http://example.com/");
        Elements docs = document.getElementsByTag("doc");
        ArrayList<String> titleList = new ArrayList<>();

        for(Element item:docs){
            String title = item.select("title").text();
            titleList.add(title); //store by Object
        }
        return titleList;
    }
}
