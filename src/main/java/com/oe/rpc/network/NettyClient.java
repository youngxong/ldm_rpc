package com.oe.rpc.network;

import com.oe.rpc.core.convert.ObjectBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyClient extends AbstractClient {

    private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private volatile HeartBeaTimer heartBeaTimer;

    private Channel channel;

    private String ip;

    private int port;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        logger.info("开始连接！");
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new LdmRpcDecoder(20));
                            ch.pipeline().addLast(new LdmRpcEncoder());
                            ch.pipeline().addLast(new HeartBeatHandler());
                            ch.pipeline().addLast(new RpcServiceHandler());
                        }
                    });
            ChannelFuture future = b.connect(ip,port).sync();

            channel = future.channel();

            //注册连接事件
            future.addListener((ChannelFutureListener)listener -> {
                //如果连接成功
                if (listener.isSuccess()) {
                    logger.info("客户端[" + listener.channel().localAddress().toString() + "]已连接...");
                    state=ACTIVE;

                }
            });

            channel.closeFuture().sync().addListener(cfl -> {
                state=CLOSE;
                logger.info("客户端监听到关闭事件...");
            });

        } catch (Exception e){
            logger.error("connect to "+ip+":"+port+" failed!!!",e);
        }finally {
            group.shutdownGracefully();
            logger.info("关闭客户端...");
        }
    }

    @Override
    public void connect() {
        close();
        if(isActive()){
            return;
        }
        Thread thread =new Thread(this);
        thread.setName("ldm-rpc-client-thread");
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public void send(LdmRpcPackage ldmRpcPackage) {
        if(channel!=null && channel.isActive()){
            logger.info("发送请求:{}", ObjectBuilder.buildPackageInfo(ldmRpcPackage));
            channel.writeAndFlush(ldmRpcPackage);
        }
    }

    @Override
    public void close() {
        if(channel!=null && channel.isActive()){
            channel.close();
        }
    }

}
