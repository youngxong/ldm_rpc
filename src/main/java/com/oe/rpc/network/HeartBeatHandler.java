package com.oe.rpc.network;

import com.oe.rpc.common.EnumHeaderType;
import com.oe.rpc.core.convert.ObjectBuilder;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by ouyongxiong on 2020/2/6.
 */
public class HeartBeatHandler extends ChannelHandlerAdapter {


    private static Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);


    HeartBeaTimer heartBeaTimer;


    @Override
    public void channelActive(ChannelHandlerContext ctx){
        heartBeaTimer=new HeartBeaTimer(60000,ctx);
        heartBeaTimer.start();
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        LdmRpcPackage ldmRpcPackage= (LdmRpcPackage)msg;
        Header header = ldmRpcPackage.getHeader();
        if(EnumHeaderType.HEARTBEAT.getCode()==header.getType()){
            logger.info("接收心跳响应:{},更新最后接收时间...",header.toString());
            heartBeaTimer.updateLastReceiveTime();
        }else {
            ctx.fireChannelRead(msg);
        }
    }


}
