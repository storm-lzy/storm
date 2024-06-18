package com.storm.im.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class WsServerMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame>{

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame) throws Exception {
        if (webSocketFrame instanceof PingWebSocketFrame) {
            log.info("WebSocket 接收到 Ping 消息");
            PingWebSocketFrame pingWebSocketFrame = (PingWebSocketFrame) webSocketFrame;
        } else if (webSocketFrame instanceof PongWebSocketFrame) {
            log.info("WebSocket 接收到 Pong 消息");
            PongWebSocketFrame pongWebSocketFrame = (PongWebSocketFrame) webSocketFrame;
        } else if (webSocketFrame instanceof TextWebSocketFrame) {
            log.info("WebSocket 接收到 Text 消息");
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) webSocketFrame;
            handleTextMessage(channelHandlerContext,textWebSocketFrame);
        } else if (webSocketFrame instanceof CloseWebSocketFrame) {
            log.info("WebSocket 接收到 Close 消息");
            CloseWebSocketFrame closeWebSocketFrame = (CloseWebSocketFrame) webSocketFrame;
        } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
            log.info("WebSocket 接收到 Binary 消息");
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) webSocketFrame;
        }
    }


    private void handleTextMessage(ChannelHandlerContext channelHandlerContext,TextWebSocketFrame textWebSocketFrame){
        String text = textWebSocketFrame.text();
        log.info("[Netty] 处理Text消息, [{}]",text);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer().writeBytes(text.getBytes());
        TextWebSocketFrame res = new TextWebSocketFrame(byteBuf);
        channelHandlerContext.writeAndFlush(res);
    }
}
