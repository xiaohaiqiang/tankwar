package com.xhq.tank.net;


import com.xhq.tank.TankFrame;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class Client {

    public static final Client INSTANCE = new Client();

    private Channel channel = null;

    private Client(){}

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
                    socketChannel.pipeline()
                            .addLast(new MsgEncoder())
                            .addLast(new MsgDecoder())
                            .addLast(new MyHandler());
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

    public void send(Msg msg) {
        channel.writeAndFlush(msg);
    }

    public void closeConnection() {
        channel.close();
    }

    static class MyHandler extends SimpleChannelInboundHandler<Msg> {



        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
            cause.printStackTrace();
            ctx.close();//future.channel.closeFuture().sync();就不再阻塞了
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(new TankJoinMsg(TankFrame.INSTANCE.getGm().getMyTank()));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Msg msg) throws Exception {
            System.out.println(msg);
            msg.handle();
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.connect();
    }
}
