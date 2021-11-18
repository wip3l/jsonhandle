package com.wxscistor.jsonhandle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author liqijian
 */
public class JsonUtils {
    static StringBuilder sb = new StringBuilder();

    /**
     * getJsonObjects递归调用器
     * @param fileUrl
     * @throws IOException
     */
    public static void jsonHandle(String fileUrl) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileUrl);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str = null;
        StringBuilder stringBuilder = new StringBuilder();
        while((str = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(str).append("\n");
        }
        List<JSONObject> jsonObjectList = JsonUtils.getJsonObjects(stringBuilder.toString());
        while (jsonObjectList.size()>0){
            List<JSONObject> newJsonObjectList = JsonUtils.getJsonObjects(jsonObjectList);
            jsonObjectList = newJsonObjectList;
        }
    }
    /**
     *  将JSONObject或JSONArray以List<JSONObject>返回
     *  将提取到到value追加到StringBuilder
     * @param str
     * @return
     */
    public static List<JSONObject> getJsonObjects(String str){
        Object object = new JSONTokener(str).nextValue();
        List<JSONObject> jsonObjectList = new ArrayList<>();
        if(object instanceof JSONObject){
            jsonObjectList.add((JSONObject)object);
        }else if (object instanceof String || object instanceof Integer || object instanceof ArrayList) {
            sb.append(object).append(",");
        }else{
            JSONArray retArray = (JSONArray)object;
            retArray.forEach(retObject->{
                if (retObject instanceof JSONObject){
                    jsonObjectList.add((JSONObject) retObject);
                }else{
                    sb.append(retObject).append(",");
                }
            });
        }
        return jsonObjectList;
    }
    public static List<JSONObject> getJsonObjects(List<JSONObject> jsonObjectList){
        List<JSONObject> newJsonObjectList = new ArrayList<>();
        jsonObjectList.forEach(retJson->{
            Iterator<String> keys = retJson.keys();
            while (keys.hasNext()){
                String key = String.valueOf(keys.next());
                String value = retJson.get(key).toString();
                List<JSONObject> cacheObjectList = getJsonObjects(value);
                newJsonObjectList.addAll(cacheObjectList);
            }
        });
        return newJsonObjectList;
    }
}