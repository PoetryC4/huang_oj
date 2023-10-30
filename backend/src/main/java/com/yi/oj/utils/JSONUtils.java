package com.yi.oj.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {
    public static <T> List<List<T>> parseJsonToListOfLists(String jsonStr, Class<T> innerType) {
        List<List<T>> result = new ArrayList<>();

        // 使用Fastjson解析JSON字符串为JSONArray
        JSONArray jsonArray = JSON.parseArray(jsonStr);

        // 遍历JSONArray，将每个内部的JSONArray转换为List<T>并添加到结果列表中
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray innerArray = jsonArray.getJSONArray(i);
            List<T> innerList = new ArrayList<>();

            for (int j = 0; j < innerArray.size(); j++) {
                T element = innerArray.getObject(j, innerType);
                innerList.add(element);
            }

            result.add(innerList);
        }

        return result;
    }
}
