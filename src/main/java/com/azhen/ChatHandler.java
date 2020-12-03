package com.azhen;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用与记录和管理所有的客户端的channel
    private  static ChannelGroup clients=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //获取从客户端传输过来的消息
        String text = msg.text();
        System.out.println("接收到的数据"+text);
        System.out.println();

        //将接收到消息发送给所有的客户端
        for (Channel client : clients) {
            //注意所有的的wenbsocket数据都应该使用text封装
            client.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息:]"+ LocalDateTime.now()+",消息为:"+text));
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将channel添加到客户端
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发 handlerRemoved，ChannelGroup 会自动移除对应客户端的 channel
        // clients.remove(ctx.channel());
        // asLongText()——唯一的 ID
        // asShortText()——短 ID（有可能会重复)
        System.out.println("客户端断开, channel 对应的长 id 为:" + ctx.channel().id().asLongText());
        System.out.println("客户端断开, channel 对应的短 id 为:" + ctx.channel().id().asShortText());
    }
}
