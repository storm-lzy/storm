package com.storm.mq.codec;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

public interface Codec {

    String id();

    default byte[] encode(Object obj) {
        return this.encode(obj, Charset.defaultCharset().name());
    }

    byte[] encode(Object var1, String var2);

    default <T> T decode(byte[] data, Class<T> type) {
        return this.decode(data, type, Charset.defaultCharset().name());
    }

    <T> T decode(byte[] var1, Class<T> var2, String var3);

    <T> T decode(byte[] var1, Type var2, String var3);
}
