package com.azhen;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WsServerInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //支持HTTP协议
        pipeline.addLast(new HttpServerCodec());
        //对大写数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //添加对HTTP请求和响应的聚合器
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //websocker服务器处理的协议,用于指定给客户端连接的路由
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //添加自定义的handle
        pipeline.addLast(new ChatHandler());
    }
}
