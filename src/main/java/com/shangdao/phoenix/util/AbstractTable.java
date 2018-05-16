package com.shangdao.phoenix.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author huanghengkun
 * @date 2018/04/10
 */
public abstract class AbstractTable {
    protected List<String> head;
    protected List<Map<String, Object>> body;

    private static final Logger logger = LoggerFactory.getLogger(AbstractTable.class);

    public List<String> getHead() {
        return head;
    }

    public List<Map<String, Object>> getBody() {
        return body;
    }

    public void setHead(String fields) {
        head = Arrays.asList(fields.split(","));
    }

    public void setBody(List<Map<String, Object>> data) {
        body = data;
    }

    public abstract String build();

    protected String convertValue(Map<String, Object> value, String head) {
        String[] split = head.split("[.]");
        Stack<String> keys = new Stack<>();
        for (int i = split.length - 1; i >= 0; i--) {
            keys.push(split[i]);
        }
        if (!keys.isEmpty()) {
            Object result = dealMap(value, keys);
            if (result == null) {
                return null;
            } else {
                return result.toString();
            }
        } else {
            return null;
        }
    }

    private Object dealMap(Map<String, Object> map, Stack<String> keys) {
        if (keys.isEmpty()) {
            return null;
        }
        String key = keys.pop();
        Object value = map.get(key);
        if (keys.isEmpty()) {
            return value;
        }
        if (value instanceof Map) {
            return dealMap((Map<String, Object>) value, keys);
        } else if (value instanceof List) {
            return dealList((List<Map<String, Object>>) value, keys);
        } else {
            return value;
        }
    }

    private Object dealList(List<Map<String, Object>> list, Stack<String> keys) {
        List<String> values = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String key = keys.peek();
            String result = dealMap(map, keys).toString();
            keys.push(key);
            values.add(result);
        }
        keys.pop();
        return String.join(",", values);
    }
}
