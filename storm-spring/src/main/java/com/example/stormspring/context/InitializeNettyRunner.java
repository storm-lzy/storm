package com.example.stormspring.context;

import com.example.stormspring.handle.ServerChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
 */
@Component
@Slf4j
public class InitializeNettyRunner implements ApplicationRunner {

    @Resource
    private ServerChannelInitializer serverChannelInitializer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        NioEventLoopGroup boos = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(2);
        ServerBootstrap b = new ServerBootstrap();
        b.group(boos, worker)
                .childHandler(serverChannelInitializer)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
        ChannelFuture channelFuture = b.bind(new InetSocketAddress(3388)).sync();
        Channel channel = channelFuture.channel();
        String ip = InetAddress.getLocalHost().getHostAddress();
        log.info("服务器 {}:{} 启动", ip, 3388);

        channel.closeFuture().addListener(future -> {
            boos.shutdownGracefully();
            worker.shutdownGracefully();
            log.info("服务器关闭...");
            System.exit(1);
        });
    }
}
