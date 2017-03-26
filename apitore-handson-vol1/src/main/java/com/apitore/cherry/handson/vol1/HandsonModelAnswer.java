package com.apitore.cherry.handson.vol1;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.apitore.banana.response.sentiment.SentimentEntity;
import com.apitore.banana.response.sentiment.SentimentResponseEntity;
import com.apitore.banana.response.twitter.TweetEntity;
import com.apitore.banana.response.twitter.TwitterResponseEntity;
import com.apitore.banana.utils.UrlFormatter;


/**
 * @author Keigo Hattori
 */
public class HandsonModelAnswer {

  static String ENDPOINT1     = "https://api.apitore.com/api/23/twitter/mytweet";
  static String ENDPOINT2     = "https://api.apitore.com/api/11/sentiment/predict";
  static String ACCESS_TOKEN  = "YOUR-ACCESS-TOKEN";

  public static void main(String[] args) {
    RestTemplate restTemplate1 = new RestTemplate();
    Map<String, String> params1 = new HashMap<String, String>();
    params1.put("access_token", ACCESS_TOKEN);
    params1.put("iter", "1");
    String url1 = UrlFormatter.format(ENDPOINT1, params1);

    TwitterResponseEntity response1 =
        restTemplate1.getForObject(url1, TwitterResponseEntity.class, params1);

    int count = 0;
    double posScore=0,negScore=0,neuScore=0;
    for (TweetEntity ent: response1.getTweets()) {
      count ++;
      if (count>10)
        break;
      String tweet = ent.getText();

      RestTemplate restTemplate2 = new RestTemplate();
      Map<String, String> params2 = new HashMap<String, String>();
      params2.put("access_token", ACCESS_TOKEN);
      params2.put("text", tweet);
      String url2 = UrlFormatter.format(ENDPOINT2, params2);

      SentimentResponseEntity response2 =
          restTemplate2.getForObject(url2, SentimentResponseEntity.class, params2);
      for (SentimentEntity ent2: response2.getSentiments()) {
        if ("positive".equals(ent2.getSentiment())) {
          posScore += ent2.getScore();
        } else if ("negative".equals(ent2.getSentiment())) {
          negScore += ent2.getScore();
        } else {
          neuScore += ent2.getScore();
        }
      }
    }
    count--;
    posScore = posScore/count * 100;
    negScore = negScore/count * 100;
    neuScore = neuScore/count * 100;

    String out = String.format("You are %.1f %% positive, %.1f %% negative, %.1f %% neutral person.", posScore,negScore,neuScore);
    System.out.println(out);
  }

}
