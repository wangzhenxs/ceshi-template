package com.azhen;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebsocketServer {
    public static void main(String[] args) {
        //初始化主线程
        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        //初始化从线程
        NioEventLoopGroup subGroup = new NioEventLoopGroup();

        try {
            //创建服务器
            ServerBootstrap b = new ServerBootstrap();
            //指定使用主线程池和从线程池   ,指定的NIO通道  指定初始化器加载通道处理器
            b.group(mainGroup,subGroup).channel(NioServerSocketChannel.class).childHandler(new WsServerInitalizer());
            //绑定端口号启动服务器,并等待服务器启动
            ChannelFuture future = b.bind(9090).sync();
            //等待服务器关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            subGroup.shutdownGracefully();
        }
    }
}
