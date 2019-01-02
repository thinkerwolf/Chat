package com.thinkerwolf.chat.nettytest;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.List;

public class NettyUdpServer {

    @Test
    public void udpTest() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(bossGroup);

        bootstrap.channel(NioDatagramChannel.class);

        bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
            protected void initChannel(NioDatagramChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new MessageToMessageDecoder<DatagramPacket>() {
                    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List <Object> out) throws Exception {
                        ByteBuf byteBuf = msg.content();
                        byte[] bs = new byte[byteBuf.readableBytes()];
                        byteBuf.readBytes(bs);
                        out.add(new String(bs));
                    }
                });
                pipeline.addLast("handler", new UdpHandler());
            }
        });

        try {
            bootstrap.bind(new InetSocketAddress(9007)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        new NettyUdpServer().udpTest();
    }

}
