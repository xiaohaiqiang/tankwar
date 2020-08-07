package com.xhq.nettychat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.Serializable;

public class Server{

    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void serverStart() {
        // 负责接客
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        // 负责服务
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            // Server启动辅助类
            ServerBootstrap b = new ServerBootstrap();
            //第一个参数负责接客，第二个参数负责服务,
            ChannelFuture future = b.group(bossGroup, workerGroup)
                    //指定异步全双工通道
                    .channel(NioServerSocketChannel.class)
                    //netty帮我们处理了accept的过程
                    .childHandler(new MyChildInitializer())
                    .bind(8888)
                    .sync();

            ServerFrame.INSTANCE.updateServerMsg("server started!");

            future.channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

class MyChildInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception{

        socketChannel.pipeline().addLast(new MyChildHandler());
    }
}

class MyChildHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
//        ByteBuf buf = (ByteBuf)msg;
//        System.out.println(buf.toString());

        ByteBuf buf = null;

        buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        String str = new String(bytes);
        if(str.equals("__bye__")){
            System.out.println("client ready ro quit");
            Server.clients.remove(ctx.channel());
            ctx.close();
            System.out.println(Server.clients.size());
        }else{
            Server.clients.writeAndFlush(buf);//writeAndFlush自动对buf释放
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        Server.clients.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.clients.add(ctx.channel());
    }
}
