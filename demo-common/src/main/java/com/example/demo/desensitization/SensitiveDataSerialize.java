package com.example.demo.desensitization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 自定义脱敏序列化类
 * Created by YuanJW on 2022/12/5.
 */
public class SensitiveDataSerialize extends JsonSerializer<String>
        implements ContextualSerializer {
    private SensitiveStrategy sensitiveStrategy;

    public SensitiveDataSerialize() {
    }

    public SensitiveDataSerialize(final SensitiveStrategy sensitiveStrategy) {
        this.sensitiveStrategy = sensitiveStrategy;
    }

    /**
     * 自定义序列化
     * @param s
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(sensitiveStrategy.desensitizer().apply(s));
    }

    /**
     * 获取注解属性
     * @param serializerProvider
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider,
                                              BeanProperty beanProperty) throws JsonMappingException {
        // 校验属性是否为空
        if (beanProperty != null) {
            // 校验属性是否为String
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
                if (sensitive != null) {
                    // 将注解的value传入SensitiveDataSerialize
                    return new SensitiveDataSerialize(sensitive.value());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
