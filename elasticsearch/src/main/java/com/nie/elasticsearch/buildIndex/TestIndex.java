package com.nie.elasticsearch.buildIndex;


import com.alibaba.fastjson.JSONObject;
import com.nie.elasticsearch.client.JestConfiguration;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.mapping.PutMapping;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

@Configuration
@EnableConfigurationProperties(JestProperties.class)
@AutoConfigureAfter(JestConfiguration.class)
public class TestIndex {

    @Autowired
    JestClient jestClient;

    public static final String INDEX = "es-article";
    public static final String TYPE = "articles";

    @Bean(initMethod = "init")
    public InitMethodBean index(){
        System.out.println("=====================zcy=============================");
        return new InitMethodBean();
    }
    private  class InitMethodBean{
        void init(){
            System.out.println("system started, triggered by initMethod property.");
            try {
                createIndex();
            }catch (Exception e){
                System.out.println(e);
            }

        }
    }



    /**
     * 创建所引
     * @throws IOException
     */
    public void createIndex() throws IOException {
        Settings.Builder builder = Settings.builder();
        builder.put("number_of_shards", 1);
        builder.put("number_of_replicas", 1);

        //创建索引
        CreateIndex createIndex = new CreateIndex.Builder(INDEX)
                .settings(builder.build().toString())
                .build();
        JestResult result = jestClient.execute(createIndex);
        checkArgument(result.isSucceeded(), result.getErrorMessage());
        createMapping();
    }
    /**
     * put映射
     * @throws IOException
     */
    public void createMapping() throws IOException {

        JSONObject objSource = new JSONObject().fluentPut("properties", new JSONObject()
                .fluentPut("title", new JSONObject()
                        .fluentPut("type", "text")
                )
                .fluentPut("author", new JSONObject()
                        .fluentPut("type", "text")
                )
                .fluentPut("content", new JSONObject()
                        .fluentPut("type", "text")
                )
                .fluentPut("publishDate", new JSONObject()
                        .fluentPut("type", "date")
                )
        );
        PutMapping putMapping = new PutMapping.Builder(INDEX,TYPE, objSource.toJSONString()).build();
        JestResult result = jestClient.execute(putMapping);

        System.out.println("【创建mapping映射成功...】");
        checkArgument(result.isSucceeded(), result.getErrorMessage());

    }

}
