package com.wxscistor.jsonhandle;


import java.io.*;


/**
 * @author liqijian
 */
public class Test {

    public static void main(String[] args) throws IOException {
        String fileUrl = "/Users/liqijian/Desktop/test2.json";
        JsonUtils.jsonHandle(fileUrl, ",");
        System.out.println(JsonUtils.sb);
    }
}
