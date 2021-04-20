import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class genSnippet {
    public static void gitSnippetFunction(String argument2, String argument4) throws IOException {
        File path = new File(argument2);
        Document document = Jsoup.parse(path, "UTF-8", "http://example.com/");
        //파일 읽어오기
        System.out.println(document); //document는 엔터가 띄어쓰기와 동일해서 사용 불가.


        String query = argument4;
        //임시로 넣어 줌
        ArrayList<String> tmpList = new ArrayList<>();
        tmpList.add("라면 밀가루 달걀 밥 생선");
        tmpList.add("라면 물 소금 반죽");
        tmpList.add("첨부 봉지면 인기");
        tmpList.add("초밥 라면 밥물 채소 소금");
        tmpList.add("초밥 종류 활어");

        ArrayList<ArrayList<String>> tmpSplitList = new ArrayList<>();
        for(int i=0;i<tmpList.size();i++){
            String[] tmp = tmpList.get(i).split(" "); //string들을 띄어쓰기로 분할
            ArrayList<String> t = new ArrayList<>();
            for(String s:tmp){
                t.add(s);
            }
            tmpSplitList.add(t);
        }


        String[] splitQuery = query.split(" ");
        for(int i=0;i<tmpSplitList.size();i++){
            if(tmpSplitList.get(i).contains(splitQuery)){

            }
        }
    }

}
