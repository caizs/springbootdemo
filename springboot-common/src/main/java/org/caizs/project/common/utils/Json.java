package org.caizs.project.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Json {
    private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share
    private static final Logger logger = LoggerFactory.getLogger(Json.class);

    // private static final String component = Character.toString((char) 31);
    // private static final String data = Character.toString((char) 29);
    // private static final String segment = Character.toString((char) 28);
    // 设置或略不存在的字段
    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    /**
     * 将对象转成json.
     *
     * @param obj
     *            对象
     * @return
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            String str = mapper.writeValueAsString(obj);
            return str;
        } catch (IOException e) {
            logger.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * json转List.
     *
     * @param <T>
     * @param content
     *            json数据
     * @param valueType
     *            泛型数据类型
     * @return
     */
    public static <T> List<T> toListObject(String content, Class<T> valueType) {
        if (content == null || content.length() == 0) {
            return null;
        }
        try {
            final CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, valueType);
            return mapper.readValue(content, javaType);
        } catch (IOException e) {
            logger.warn("message:" + e.getMessage() + " content:" + content);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toObject(List<String> jsonList, Class<T> valueType) {
        if (jsonList == null || jsonList.isEmpty()) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        for (String json : jsonList) {
            list.add(Json.toObject(json, valueType));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String content) {
        return Json.toObject(content, Map.class);
    }

    @SuppressWarnings("unchecked")
    public static Set<Object> toSet(String content) {
        return Json.toObject(content, Set.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> toMap(String json, Class<T> clazz) {
        return Json.toObject(json, Map.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        return Json.toObject(json, Set.class);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toNotNullMap(String json) {
        Map<String, Object> map = Json.toObject(json, Map.class);
        if (map == null) {
            map = new LinkedHashMap<String, Object>();
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> toNotNullMap(String json, Class<T> clazz) {
        Map<String, T> map = Json.toObject(json, Map.class);
        if (map == null) {
            map = new LinkedHashMap<String, T>();
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<T> toNotNullSet(String json, Class<T> clazz) {
        Set<T> set = Json.toObject(json, Set.class);
        if (set == null) {
            set = new LinkedHashSet<T>();
        }
        return set;
    }

    /**
     * 类型转换.
     *
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> T convert(Object obj, Class<T> clazz) {
        String json = Json.toJson(obj);
        return toObject(json, clazz);
    }

    /**
     * 将Json转换成对象.
     *
     * @param json
     * @param valueType
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.warn("message:" + e.getMessage() + " json:" + json);
            throw new RuntimeException(e);
        }
    }

    public static void print(Object obj) {
        String json = Json.toJson(obj);
        System.out.println("json:" + json);
    }

    public static void print(Object obj, String name) {
        String json = Json.toJson(obj);
        System.out.println("json info " + name + "::" + json);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void printMap(Map map, String name) {
        if (map == null) {
            System.out.println("json info " + name + "::null");
            return;
        }
        if (map.size() == 0) {
            System.out.println("json info " + name + "::");
            return;
        }
        Iterator<Entry> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("key:" + key + " json:" + Json.toJson(value));
        }

    }

    @SuppressWarnings({ "rawtypes" })
    public static void printList(List list, String name) {
        if (list == null) {
            System.out.println("json info " + name + "::null");
            return;
        }
        if (list.size() == 0) {
            System.out.println("json info " + name + "::");
            return;
        }
        for (Object element : list) {
            System.out.println("json info " + name + "::" + Json.toJson(element));
        }

    }

    public static void main(String[] args) {

    }

}
