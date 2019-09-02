package com.nie.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;

@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MyApplication.class,args);
        //阻塞主线程
        MyApplication.class.wait();
    }
}
