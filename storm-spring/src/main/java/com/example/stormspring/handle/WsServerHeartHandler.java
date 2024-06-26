package com.example.stormspring.handle;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务器心跳处理handler 未按时发送心跳的客户端会被close
 *
 * @description:
 * @author: zhou miao
 * @create: 2022/04/09
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class WsServerHeartHandler extends ChannelDuplexHandler {
    @Resource
    private ChannelManager channelManager;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 删除无用通道
                log.info("未及时发送心跳 关闭连接 {}", ctx.channel());
                channelManager.remove(ctx.channel());
                return;
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
