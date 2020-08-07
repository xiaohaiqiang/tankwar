package com.xhq.nettychat;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.sql.SQLOutput;

public class Client {

    private Channel channel = null;

    public void connect(){
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        Bootstrap b = new Bootstrap();
        try{
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception{
                    channel = socketChannel;
                    socketChannel.pipeline().addLast(new MyHandler());
                }
            });

            ChannelFuture future = b.connect("localhost", 8888).sync();

            System.out.println("connected to server");

            //阻塞，当channel的close事件发生时，closeFuture才能拿到future
            //没人调用close方法时就阻塞了。
            //等待关闭。
            future.channel().closeFuture().sync();
            System.out.println("go on");
            //System.in.read();//阻塞
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }

    }

    public void send(String text) {
        channel.writeAndFlush(Unpooled.copiedBuffer(text.getBytes()));
    }

    public void closeConnection() {
        send("__bye__");
        channel.close();
    }

    static class MyHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
            ByteBuf buf = null;
            try {
                buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.getBytes(buf.readerIndex(), bytes);
                String str = new String(bytes);
                ClientFrame.INSTANCE.updateText(str);
//            System.out.println(str);
//            System.out.println(buf.refCnt());
            }finally {
                if (buf != null) {
                    ReferenceCountUtil.release(buf);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
            cause.printStackTrace();
            ctx.close();//future.channel.closeFuture().sync();就不再阻塞了
        }
    }
}
