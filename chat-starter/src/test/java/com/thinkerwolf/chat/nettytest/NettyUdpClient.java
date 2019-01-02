package com.thinkerwolf.chat.nettytest;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.junit.Test;

import java.util.List;

public class NettyUdpClient {
    @Test
    public void udpTest() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(bossGroup);

        bootstrap.channel(NioDatagramChannel.class);

        bootstrap.handler(new ChannelInitializer <NioDatagramChannel>() {
            protected void initChannel(NioDatagramChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new MessageToMessageDecoder <DatagramPacket>() {
                    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List <Object> out) throws Exception {
                        out.add(msg.content().toString());
                    }
                });
                pipeline.addLast("handler", new UdpHandler());
                pipeline.addLast("encode", new MessageToByteEncoder<String>() {
                    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
                        out.writeBytes(msg.getBytes());
                    }
                });

            }
        });

        try {
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 9007).sync();
            ChannelFuture writecf = cf.channel().writeAndFlush("Hello web");
            cf.sync();


            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
