package com.example.stormspring.handle;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhou miao
 * @create: 2022/04/15
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Resource
    private WsServerCommonHandler wsServerCommonHandler;
    @Resource
    private WsServerHandshakeHandler wsServerHandshakeHandler;
    @Resource
    private WsServerHeartHandler wsServerHeartHandler;
    @Resource
    private WsServerMessageHandler wsServerMessageHandler;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(20, 0, 0));
        // 心跳处理
        pipeline.addLast(wsServerHeartHandler);
        // 关闭或者异常处理
        pipeline.addLast(wsServerCommonHandler);
        //以下三个是Http的支持
        //http解码器
        pipeline.addLast(new HttpServerCodec());
        //http聚合器
        pipeline.addLast(new HttpObjectAggregator(1024 * 3));
        //支持写大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        // 握手处理
        pipeline.addLast(wsServerHandshakeHandler);
        // WebSocket数据压缩
        pipeline.addLast(new WebSocketServerCompressionHandler());
        //websocket支持,设置路由
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", true));
        // 消息处理
        pipeline.addLast(wsServerMessageHandler);
    }


}
