package com.apitore.cherry.handson.vol2;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.apitore.banana.response.com.atilika.kuromoji.TokenEntity;
import com.apitore.banana.response.com.atilika.kuromoji.TokenResponseEntity;
import com.apitore.banana.response.org.rome.FeedEntryEntity;
import com.apitore.banana.response.org.rome.FeedResponseEntity;
import com.apitore.banana.utils.UrlFormatter;


/**
 * @author Keigo Hattori
 */
public class HandsonModelAnswer {

  static String ENDPOINT1     = "https://api.apitore.com/api/35/feeds/tech";
  static String ENDPOINT2     = "https://api.apitore.com/api/7/kuromoji-ipadic-neologd/tokenize";
  static String ACCESS_TOKEN  = "YOUR-ACCESS-TOKEN";

  public static void main(String[] args) {
    RestTemplate restTemplate1 = new RestTemplate();
    Map<String, String> params1 = new HashMap<String, String>();
    params1.put("access_token", ACCESS_TOKEN);
    params1.put("page", "1");
    String url1 = UrlFormatter.format(ENDPOINT1, params1);

    FeedResponseEntity response1 =
        restTemplate1.getForObject(url1, FeedResponseEntity.class, params1);

    int count = 0;
    for (FeedEntryEntity ent: response1.getEntries()) {
      count ++;
      if (count>10)
        break;
      String title = ent.getTitle();

      RestTemplate restTemplate2 = new RestTemplate();
      Map<String, String> params2 = new HashMap<String, String>();
      params2.put("access_token", ACCESS_TOKEN);
      params2.put("text", title);
      String url2 = UrlFormatter.format(ENDPOINT2, params2);

      TokenResponseEntity response2 =
          restTemplate2.getForObject(url2, TokenResponseEntity.class, params2);
      for (TokenEntity ent2: response2.getTokens()) {
        if ("名詞".equals(ent2.getPartOfSpeechLevel1())) {
          System.out.print(ent2.getSurface()+" ");
        }
      }
      System.out.println();
    }
    count--;
  }

}
