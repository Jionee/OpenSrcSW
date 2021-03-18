import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

import java.util.ArrayList;

public class makeKeyword {
    public static String kkmaString(ArrayList<Item> itemList, int i) {
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
}
