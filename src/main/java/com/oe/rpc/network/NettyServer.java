package com.oe.rpc.network;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NettyServer extends AbstractServer {



    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private String ip;

    private int port;


    private  Channel serverChannel;

    public NettyServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public  void start() throws Exception{
        if(isRunning()){
            return;
        }
        Thread thread =new Thread(this);
        thread.setName("ldm-rpc-server-thread");
        thread.setDaemon(true);
        thread.start();
    }

    public void close() {
        if(serverChannel!=null){
            serverChannel.close();
            serverChannel=null;
        }
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws IOException {
                            ch.pipeline().addLast(new LdmRpcDecoder(20));
                            ch.pipeline().addLast(new LdmRpcEncoder());
                            ch.pipeline().addLast(new RpcServiceHandler());
                        }
                    });


            // 绑定端口，同步等待成功
            ChannelFuture serverChannel = b.bind(ip,port).sync();
            state=RUNNING;
            logger.info("ldm-rpc server start  success,listening port:"+port+" !");
            // 等待服务端监听端口关闭
            serverChannel.channel().closeFuture().sync();
        }catch (Exception e){
            logger.error("ldm-rpc server start failed!!!");
        }finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }
}
