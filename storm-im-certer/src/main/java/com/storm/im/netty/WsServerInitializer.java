package com.storm.im.netty;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.storm.im.netty.handler.WsServerHeartHandler;
import com.storm.im.netty.handler.WsServerMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
@Slf4j
public class WsServerInitializer implements CommandLineRunner {

    private static volatile boolean isStart = false;
    private NioEventLoopGroup boos;
    private NioEventLoopGroup worker;
    private static int port = 9000;

    @Override
    public void run(String... args) {
        try {
            boos = new NioEventLoopGroup(1);
            worker = new NioEventLoopGroup(1);
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boos, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .childHandler(new WxServerChannelInitializer());

            ChannelFuture future = serverBootstrap.bind(port).sync();
            Channel channel = future.channel();
            isStart = true;
            log.info("[Netty] 服务器启动成功, 端口[{}]",port);
            channel.closeFuture().addListener(new WsServerChannelFuture());
        } catch (InterruptedException e) {
            shutdown();
        }
    }

    private class WsServerChannelFuture implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            shutdown();
        }
    }

    private void shutdown() {
        if (null != boos) {
            boos.shutdownGracefully();
        }
        if (null != worker) {
            worker.shutdownGracefully();
        }
    }

    protected boolean isStart(){
        return isStart;
    }

    private static class WxServerChannelInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new IdleStateHandler(60, 0, 0));
            // 心跳处理
            pipeline.addLast(new WsServerHeartHandler());
//            http解码器
            pipeline.addLast(new HttpServerCodec());
            //http聚合器
            pipeline.addLast(new HttpObjectAggregator(100));
            //支持写大数据流
            pipeline.addLast(new ChunkedWriteHandler());

            // WebSocket数据压缩
            pipeline.addLast(new WebSocketServerCompressionHandler());

            //websocket支持,设置路由
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", true));

            // 编码器，转换为二进制帧处理
            pipeline.addLast(new ProtobufToWebSocketFrameEncoder());

            pipeline.addLast(new WsServerMessageHandler());
        }
    }


    static class ProtobufToWebSocketFrameEncoder extends MessageToMessageEncoder<TextWebSocketFrame>{

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame messageLiteOrBuilder, List<Object> list) throws Exception {
            TextWebSocketFrame touch = messageLiteOrBuilder.touch();
            WebSocketFrame frame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(touch.text().getBytes()));
            list.add(frame);
        }
    }
}
