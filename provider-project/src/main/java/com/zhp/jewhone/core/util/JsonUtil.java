package com.zhp.jewhone.core.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zhp.jewhone.core.util.json.MyBeanSerializerModifier;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Json Util
 *
 * @time 2017年1月6日 下午2:32:57
 */
public class JsonUtil {
   private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 去除默认的时间格式
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 设置时区为中国上海
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        // 序列化时 NULL 的处理
        OBJECT_MAPPER.setSerializationInclusion(Include.ALWAYS);
        // 反序列化时，属性不存在的兼容处理
        OBJECT_MAPPER.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 序列化时，统一的时间格式处理
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 单引号的处理
        OBJECT_MAPPER.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //处理序列化时"",null,0转换混乱的问题
       // OBJECT_MAPPER.setSerializerFactory(OBJECT_MAPPER.getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
    }

    /**
     * String to Generic
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            if (StringUtils.isNotEmpty(json))
                return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonParseException ignored) {
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * json转bean
     *
     * @param jsonStr
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toBean(String jsonStr, Class<?> clazz) {
        try {
            // JSON转换
            JSONObject jsonObj = JSONObject.fromObject(jsonStr);
            return (T) JSONObject.toBean(jsonObj, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(String jsonStr, Class<?> clazz, Map<String, Class<?>> classMap) {
        try {
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd HH:mm:ss"}));
            // JSON转换
            JSONObject jsonObj = JSONObject.fromObject(jsonStr);
            return (T) JSONObject.toBean(jsonObj, clazz, classMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转list bean
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> toListBean(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    public static <T> T toObject(Object object, Class<T> clazz) {
        return toObject(JsonUtil.toJson(object), clazz);
    }

    /**
     * String to Map
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object json) {
        return toObject(json, HashMap.class);
    }

    /**
     * Generic to String
     *
     * @param src
     * @return
     */
    public static <T> String toJson(T src) {
        try {
            if (src instanceof String) {
                return (String) src;
            } else {
                return OBJECT_MAPPER.writeValueAsString(src);
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    /**
     * Json to Collection
     *
     * @param json
     * @param typeReference
     * @return
     */
    public static <T> T toCollection(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException ignored) {
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("xxx", null);
        map.put("qqq", "");
        map.put("aaa",new ArrayList<>());
        map.put("ccc",0);
        System.out.println(JsonUtil.toJson(map));
    }
}
