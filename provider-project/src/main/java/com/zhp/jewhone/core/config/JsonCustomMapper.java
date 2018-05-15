package com.zhp.jewhone.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zhp.jewhone.core.util.json.MyBeanSerializerModifier;
import org.springframework.context.annotation.Configuration;

/**
 * 处理对象转json序列化问题
 */
@Configuration
public class JsonCustomMapper extends ObjectMapper {
    private static final long serialVersionUID = -7172005990911448107L;

    public JsonCustomMapper() {
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 设置 SerializationFeature.FAIL_ON_EMPTY_BEANS 为 false
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 字段和值都加引号
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 数字不加引号
        this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, false);
        this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, false);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 空值处理为空串
        this.setSerializerFactory(this.getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
    }
}
