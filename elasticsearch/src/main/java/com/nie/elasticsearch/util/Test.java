package com.nie.elasticsearch.util;

import java.io.File;


public class Test {

    public static void main(String[] args) {
        File file = new File("D:\\github\\git\\trading-microservice-productsalepolicy\\productsalepolicy-core\\src\\main\\java\\com\\yijiupi\\himalaya\\trading\\productsalepolicy\\service");
        System.out.println("sd");
        if(file.isDirectory()){
            File[] fs = file.listFiles();
            for(File f : fs){
                System.out.println(f.getName().substring(0,f.getName().indexOf(".")));
            }

        }
    }

}
