package com.olive.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

// 这里TextWebSocketFrame类型，表示一个文本帧（frame）
public class CustomTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息：" + msg.text());

        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间：" + LocalDateTime.now() + " msg:" + msg.text()));
    }

    // web连接后触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // LongText是唯一的，ShortText不一定是唯一的
        System.out.println("handlerAdded被调用了......" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded被调用了......" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved被调用了......" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常：" + cause.getMessage());
        ctx.close();
    }
}
