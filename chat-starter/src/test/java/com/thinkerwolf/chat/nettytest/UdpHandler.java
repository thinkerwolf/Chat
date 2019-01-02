package com.thinkerwolf.chat.nettytest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class UdpHandler extends SimpleChannelInboundHandler<String> {
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        System.out.println("UpdHandler msg " + msg);
        ctx.channel().writeAndFlush("Form server Fuck you");
    }
}
