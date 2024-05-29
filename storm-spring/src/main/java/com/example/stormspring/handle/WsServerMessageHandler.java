package com.example.stormspring.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 处理websocket中的消息
 *
 * @author zhou miao
 * @date 2022/04/09
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class WsServerMessageHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Resource
    private ChannelManager channelManager;
    @Resource
    private ApplicationContext applicationContext;

    private final Object lock = new Object();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        if (webSocketFrame instanceof PingWebSocketFrame) {
            pingWebSocketFrameHandler(ctx, (PingWebSocketFrame) webSocketFrame);
        } else if (webSocketFrame instanceof TextWebSocketFrame) {
            System.out.println(((TextWebSocketFrame) webSocketFrame).text());
        } else if (webSocketFrame instanceof CloseWebSocketFrame) {
            closeWebSocketFrameHandler(ctx, (CloseWebSocketFrame) webSocketFrame);
        } else if (webSocketFrame instanceof BinaryWebSocketFrame) {
            System.out.println("消息处理");
        }
    }

    /**
     * 客户端发送断开请求处理
     *
     * @param ctx 通道上下文
     * @param frame 关闭消息体
     */
    private void closeWebSocketFrameHandler(ChannelHandlerContext ctx, CloseWebSocketFrame frame) {
        log.info("{} 申请关闭 {}", ctx.channel(), frame);
        channelManager.remove(ctx.channel());
    }

    /**
     * 处理客户端心跳包
     *
     * @param ctx 通道上下文
     * @param frame 关闭消息体
     */
    private void pingWebSocketFrameHandler(ChannelHandlerContext ctx, PingWebSocketFrame frame) {
        log.info("客户端发送心跳请求 {} {}", ctx.channel(), frame);
        ctx.channel().writeAndFlush(new PongWebSocketFrame());
    }

}
