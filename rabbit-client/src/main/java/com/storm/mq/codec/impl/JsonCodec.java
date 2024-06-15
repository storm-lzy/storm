package com.storm.mq.codec.impl;

import cn.hutool.core.util.StrUtil;
import com.storm.mq.codec.Codec;
import com.storm.mq.codec.enums.CodecType;
import com.storm.mq.utils.JSONUtil;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class JsonCodec implements Codec {

    @Override
    public String id() {
        return CodecType.JSON.name();
    }

    @Override
    public byte[] encode(Object obj, String charsetName) {
        byte[] data = JSONUtil.write(obj);
        Charset charset;
        return !StrUtil.isBlank(charsetName) && !(charset = Charset.forName(charsetName)).equals(StandardCharsets.UTF_8) ? (new String(data, StandardCharsets.UTF_8)).getBytes(charset) : data;
    }

    @Override
    public <T> T decode(byte[] data, Class<T> type, String charsetName) {
        Charset charset;
        if (!StrUtil.isBlank(charsetName) && !(charset = Charset.forName(charsetName)).equals(StandardCharsets.UTF_8)) {
            byte[] utf8Data = (new String(data, charset)).getBytes(StandardCharsets.UTF_8);
            return JSONUtil.read(utf8Data, type);
        } else {
            return JSONUtil.read(data, type);
        }
    }

    @Override
    public <T> T decode(byte[] data, Type type, String charsetName) {
        Charset charset;
        if (!StrUtil.isBlank(charsetName) && !(charset = Charset.forName(charsetName)).equals(StandardCharsets.UTF_8)) {
            byte[] utf8Data = (new String(data, charset)).getBytes(StandardCharsets.UTF_8);
            return JSONUtil.read(utf8Data, type);
        } else {
            return JSONUtil.read(data, type);
        }
    }
}
