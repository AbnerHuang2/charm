package com.abner.charm.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author: huanghousheng
 * @date: 2021/4/19
 * @description:
 */
@Slf4j
public class CharmServer {

    private final int PORT = 8899;

    public static void main(String[] args) throws InterruptedException {
        CharmServer charmServer = new CharmServer();
        charmServer.start();
    }

    private void start() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new IdleStateHandler(0,0,30 * 3, TimeUnit.SECONDS)) //beat 3N, close if idle
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(5 * 1024 * 1024))   //http必加，否则没有返回
                                .addLast(new CharmChannelHandler());
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        // bind
        ChannelFuture future = bootstrap.bind(PORT).sync();

        log.info(">>>>>>>>>>> charm-server start success, nettype = {}, port = {}", CharmServer.class, PORT);

        // wait util stop
        future.channel().closeFuture().sync();
    }

}
