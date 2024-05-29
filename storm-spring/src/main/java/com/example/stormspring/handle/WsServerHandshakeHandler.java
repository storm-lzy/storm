package com.example.stormspring.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 处理ws握手请求handler
 *
 * @author zhou miao
 * @date 2022/04/09
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WsServerHandshakeHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private ChannelManager channelManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            log.debug("握手请求 {}", msg);
            if (!handleHandshakeSuccess((FullHttpRequest) msg, ctx)) {
                log.error("连接信息无效 {}", msg);
                ctx.close();
                return;
            }
        }
        super.channelRead(ctx, msg);
    }

    /**
     * 处理握手请求 将用户连接信息加入到redis缓存
     *
     * @param fullHttpRequest 握手请求
     * @param ctx             通道上下文
     */
    private boolean handleHandshakeSuccess(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        log.info("握手处理器{}",fullHttpRequest);
        return true;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("服务端连接发生错误 {} {}", ctx.channel(), cause.getMessage());
    }
}
