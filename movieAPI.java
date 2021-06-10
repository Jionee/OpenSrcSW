import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class movieAPI {
    public static void main(String args[]) throws IOException, ParseException {
        Credentials credential = new Credentials(); //ClientId, CliendSecret 포함 파일(gitIgnore)에 등록
        String keyword = "기생충";
        String query = URLEncoder.encode(keyword,"UTF-8");
        String apiURL = "https://openapi.naver.com/v1/search/movie.json?query=" + query;

        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-Naver-Client-Id",credential.getClientID());
        con.setRequestProperty("X-Naver-Client-Secret",credential.getClientSecret());

        //API응답
        int responseCode = con.getResponseCode();
        BufferedReader br;
        if(responseCode==200){
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }
        else{
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        //읽기
        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = br.readLine()) != null){
            response.append(inputLine);
        }
        br.close();


        //JSON
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(String.valueOf(response));
        JSONArray jsonArray = (JSONArray) jsonObject.get("items");

        //기생충 검색 시 5개 뜸
        for(int i=0;i<jsonArray.size();i++){
            System.out.println("=item_"+i+"=====================================");
            JSONObject itemObject = (JSONObject) jsonArray.get(i);
            System.out.println("title:\t" + itemObject.get("title"));
            System.out.println("link:\t" + itemObject.get("link"));
            System.out.println("subtitle:\t" + itemObject.get("subtitle"));
            System.out.println("pubDate:\t" + itemObject.get("pubDate"));
            System.out.println("director:\t" + itemObject.get("director"));
            System.out.println("actor:\t" + itemObject.get("actor"));
            System.out.println("userRating:\t" + itemObject.get("userRating"));
        }
    }
}
