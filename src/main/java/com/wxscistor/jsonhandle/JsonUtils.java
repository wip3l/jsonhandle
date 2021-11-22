package com.wxscistor.jsonhandle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
    public static void jsonHandle(String fileUrl, String split) throws IOException {
        FileInputStream inputStream = new FileInputStream(fileUrl);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        StringBuilder stringBuilder = new StringBuilder();
        while((str = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(str).append("\n");
        }
        List<JSONObject> jsonObjectList = JsonUtils.getJsonObjects(stringBuilder.toString(),split);
        while (jsonObjectList.size()>0){
            List<JSONObject> newJsonObjectList = JsonUtils.getJsonObjects(jsonObjectList, split);
            jsonObjectList = newJsonObjectList;
        }
    }
    /**
     *  将JSONObject或JSONArray以List<JSONObject>返回
     *  将提取到到value追加到StringBuilder
     * @param str
     * @return
     */
    public static List<JSONObject> getJsonObjects(String str, String split){
        Object object = new JSONTokener(str).nextValue();
        System.out.println("JSON内容是 :   "+str);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        if(object instanceof JSONObject){
            jsonObjectList.add((JSONObject)object);
        }else if (object instanceof String || object instanceof Integer ||
                object instanceof ArrayList || object instanceof Long ||
                object instanceof BigDecimal || object instanceof Boolean) {
            sb.append(object).append(split);
            System.out.println(object.toString());
        }else{
            JSONArray retArray = (JSONArray)(Objects.requireNonNull(object));
            retArray.forEach(retObject->{
                if (retObject instanceof JSONObject){
                    jsonObjectList.add((JSONObject) retObject);
                }else{
                    sb.append(retObject).append(split);
                    System.out.println(retObject.toString());
                }
            });
        }
        return jsonObjectList;
    }
    public static List<JSONObject> getJsonObjects(List<JSONObject> jsonObjectList, String split){
        List<JSONObject> newJsonObjectList = new ArrayList<>();
        jsonObjectList.forEach(retJson->{
            Iterator<String> keys = retJson.keys();
            while (keys.hasNext()){
                String key = String.valueOf(keys.next());
                String value = "null".equals(retJson.get(key).toString()) || "".equals(retJson.get(key).toString()) ?
                        "[]" : retJson.get(key).toString();
                List<JSONObject> cacheObjectList = getJsonObjects(value, split);
                newJsonObjectList.addAll(cacheObjectList);
            }
        });
        return newJsonObjectList;
    }

}