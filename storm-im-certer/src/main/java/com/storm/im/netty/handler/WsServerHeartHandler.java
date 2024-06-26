package com.storm.im.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class WsServerHeartHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            log.info("[Netty] 心跳状态：{}",idleStateEvent.state());
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 删除无用通道
                log.info("未及时发送心跳 关闭连接 {}", ctx.channel());
                ctx.close();
                return;
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
