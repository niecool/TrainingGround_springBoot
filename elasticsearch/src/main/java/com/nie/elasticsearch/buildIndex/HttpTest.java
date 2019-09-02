package com.nie.elasticsearch.buildIndex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.nie.elasticsearch.client.MyHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@AutoConfigureAfter(MyHttpClient.class)
public class HttpTest {
    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void postMyRequest(){
        URI uri = null;
        try {
            uri = new URI("http://localhost:9200/my_index/my_index_type/_search");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JSONObject postData = JSON.parseObject("{\n" +
                "    \"query\": {\n" +
                "        \"function_score\": {\n" +
                "            \"query\": {\n" +
                "                \"bool\": {\n" +
                "                    \"must\": [\n" +
                "                        {\n" +
                "                            \"term\": {\n" +
                "                                \"noScoreSearchWords\": \"赵成业\"\n" +
                "                            }\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            },\n" +
                "            \"script_score\": {\n" +
                "                \"script\": {\n" +
                "                    \"lang\": \"painless\",\n" +
                "                    \"source\": \"_score * ((doc['noScoreSearchWords'].values.contains('小米')) ? 10:1)\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");
        JsonObject jsonObject = restTemplate.postForObject(uri,postData, JsonObject.class);
        System.out.println(jsonObject);
    }
}
