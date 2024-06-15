package com.storm.mq.utils;

import com.sun.xml.internal.ws.util.UtilException;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class ClassUtil {

    private static final String CLASS_PREFIX = "class ";

    public static <T> T newInstance(Class<T> type) {
        try {
            boolean isMemberClass = type.isMemberClass();
            if (!isMemberClass) {
                return type.newInstance();
            } else {
                List<Class<?>> cs = new ArrayList<>();
                cs.add(type);

                for(Class<?> cc = type; cc.isMemberClass(); cc = cc.getEnclosingClass()) {
                    cs.add(0, cc.getEnclosingClass());
                }

                Object current = null;

                for (Class<?> c : cs) {
                    if (current == null) {
                        current = c.getDeclaredConstructor().newInstance();
                    } else {
                        current = c.getDeclaredConstructor(current.getClass()).newInstance(current);
                    }
                }

                return (T) current;
            }
        } catch (Exception var7) {
            throw new UtilException(var7);
        }
    }

    public static boolean isInterface(Type type) {
        return type.toString().indexOf("interface ") == 0;
    }

    public static boolean isClass(Type type) {
        return type.toString().indexOf("class ") == 0;
    }

    public static boolean isPrimitive(Type type) {
        return "int".equals(type.toString()) || "double".equals(type.toString()) || "short".equals(type.toString()) || "long".equals(type.toString()) || "float".equals(type.toString()) || "boolean".equals(type.toString()) || "char".equals(type.toString()) || "byte".equals(type.toString());
    }

    public static boolean isGenericType(Type type) {
        return type.toString().indexOf("<") > 0;
    }

    public static Type getType(String name) {
        Object type;
        switch (name) {
            case "int":
                type = Integer.TYPE;
                break;
            case "boolean":
                type = Boolean.TYPE;
                break;
            case "byte":
                type = Byte.TYPE;
                break;
            case "short":
                type = Short.TYPE;
                break;
            case "long":
                type = Long.TYPE;
                break;
            case "char":
                type = Character.TYPE;
                break;
            case "float":
                type = Float.TYPE;
                break;
            case "double":
                type = Double.TYPE;
                break;
            default:
                if (name.startsWith("class ") && name.length() > 6) {
                    type = forName(name.substring(6).trim());
                } else {
                    if (!name.contains("<")) {
                        throw new IllegalArgumentException(String.format("class [%s] notfound ", name));
                    }

                    type = getParameterizedType(name);
                }
        }

        return (Type)type;
    }

    public static ParameterizedType getParameterizedType(String name) {
        int begin = name.indexOf("<");
        int end = name.lastIndexOf(">");
        String className = name.substring(0, begin);
        String paraTypes = name.substring(begin + 1, end);
        Class aClass = forName(className);
        String[] split = paraTypes.split(",");
        Type[] innerTypes = new Type[split.length];

        for(int i = 0; i < split.length; ++i) {
            String s = split[i];
            if (s.contains("<")) {
                innerTypes[i] = getParameterizedType(s);
            } else {
                if (s.endsWith("[]")) {
                    s = "class [L" + s.substring(0, s.length() - 2).trim() + ";";
                } else {
                    s = "class " + s;
                }

                innerTypes[i] = getType(s);
            }
        }

        return ParameterizedTypeImpl.make(aClass, innerTypes, (Type)null);
    }

    public static Class forName(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (Exception var2) {
            throw new UtilException(var2);
        }
    }

}
