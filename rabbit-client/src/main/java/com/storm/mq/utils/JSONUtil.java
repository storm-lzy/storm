package com.storm.mq.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.util.UtilException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JSONUtil {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T read(byte[] data, Class<T> type) {
        if (data != null && data.length != 0 && type != null) {
            try {
                return objectMapper.readValue(data, type);
            } catch (IOException var3) {
                throw new UtilException(var3);
            }
        } else {
            return null;
        }
    }

    public static <T> T read(byte[] data, final Type type) {
        if (data != null && data.length != 0 && type != null) {
            try {
                return objectMapper.readValue(data, new TypeReference<T>() {
                    public Type getType() {
                        return type;
                    }
                });
            } catch (Throwable var3) {
                throw new UtilException(var3);
            }
        } else {
            return null;
        }
    }

    public static byte[] write(Object data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException var2) {
            throw new UtilException(var2);
        }
    }

    public static String toString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var2) {
            throw new UtilException(var2);
        }
    }

    public static Map toMap(String str) {
        try {
            return objectMapper.readValue(str, Map.class);
        } catch (JsonProcessingException var2) {
            throw new UtilException(var2);
        }
    }

    public static List toList(String str) {
        try {
            return objectMapper.readValue(str, List.class);
        } catch (JsonProcessingException var2) {
            throw new UtilException(var2);
        }
    }

    public static <T> List<T> toList(String str, final Class<T> type) {
        try {
            return (List<T>)objectMapper.readValue(str, new TypeReference<List<T>>() {
                public Type getType() {
                    return ClassUtil.getType("java.util.ArrayList<" + type.getName() + ">");
                }
            });
        } catch (JsonProcessingException var3) {
            throw new UtilException(var3);
        }
    }

    public static <T> List<T> toList(byte[] data, final Class<T> type) {
        try {
            return (List<T>)objectMapper.readValue(data, new TypeReference<List<T>>() {
                public Type getType() {
                    return ClassUtil.getType("java.util.ArrayList<" + type.getName() + ">");
                }
            });
        } catch (IOException var3) {
            throw new UtilException(var3);
        }
    }

    public static <T> T toObject(String str, final Type type) {
        try {
            return objectMapper.readValue(str, new TypeReference<T>() {
                public Type getType() {
                    return type;
                }
            });
        } catch (JsonProcessingException var3) {
            throw new UtilException(var3);
        }
    }

    public static <T> T toObject(String str, Class<T> clazz) {
        try {
            return objectMapper.readValue(str, clazz);
        } catch (JsonProcessingException var3) {
            throw new UtilException(var3);
        }
    }

    private JSONUtil() {
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
    }
}
