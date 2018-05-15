package com.zhp.jewhone.core.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * 序列化初始
 */
public class MyBeanSerializerModifier extends BeanSerializerModifier {

    private JsonSerializer<Object> _nullArrayJsonSerializer = new MyNullArrayJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
		for (BeanPropertyWriter writer : beanProperties) {
			// 判断字段的类型，如果是array，list，set则注册nullSerializer
			if (isArrayType(writer)) {
				//给writer注册一个自己的nullSerializer
				writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
			} else if (isIntType(writer)) {
				writer.assignNullSerializer(new JsonSerializer<Object>() {
					@Override
					public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException {
						jg.writeObject(0);
					}
				});
			} else if (isDoubleType(writer)) {
				writer.assignNullSerializer(new JsonSerializer<Object>() {
					@Override
					public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException {
						jg.writeObject(0.0);
					}
				});
			} else {
				writer.assignNullSerializer(new JsonSerializer<Object>() {
					@Override
					public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException {
						jg.writeString("");
					}
				});
			}
		}
        return beanProperties;
    }

    // 判断是什么类型
	private boolean isArrayType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getType().getRawClass();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);

    }
	private boolean isIntType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getType().getRawClass();
    	return clazz.equals(Short.class) || clazz.equals(Integer.class) || clazz.equals(Long.class);
    	
    }
	private boolean isDoubleType(BeanPropertyWriter writer) {
		Class<?> clazz = writer.getType().getRawClass();
		return clazz.equals(Double.class);
		
	}
    private JsonSerializer<Object> defaultNullArrayJsonSerializer() {
        return _nullArrayJsonSerializer;
    }
}
