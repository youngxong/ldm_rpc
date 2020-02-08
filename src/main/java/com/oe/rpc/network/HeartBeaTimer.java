package com.oe.rpc.network;

import com.oe.rpc.core.convert.ObjectBuilder;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ouyongxiong on 2020/2/7.
 */
public class HeartBeaTimer {

    private static Logger logger = LoggerFactory.getLogger(HeartBeaTimer.class);

    private ScheduledThreadPoolExecutor heartBeat;

    private volatile long lastReceive;

    private  long maxTimeoutTime;

    private volatile ChannelHandlerContext cxt;

    public HeartBeaTimer(long maxTimeoutTime, ChannelHandlerContext cxt) {
        this.maxTimeoutTime = maxTimeoutTime;
        this.cxt = cxt;
    }


    public void start(){
        //开启定时发送心跳线程
        heartBeat=new ScheduledThreadPoolExecutor(1);
        heartBeat.scheduleAtFixedRate(new HeartBeatTask(),0,3000, TimeUnit.MILLISECONDS);
    };

    public void updateLastReceiveTime(){
        lastReceive=now();
    }

    private class HeartBeatTask implements Runnable {

        @Override
        public void run() {
            //初始化
            if(lastReceive==0){
                lastReceive=now();
            }
            //检查是否超时
            long timeout = now() - lastReceive;
            if(timeout>maxTimeoutTime){
                //关闭客户端
                cxt.close();
                //关闭线程池
                heartBeat.shutdown();
            }else {
                LdmRpcPackage ldmRpcPackage = ObjectBuilder.buildHeartBeatReq();
                logger.info("发送心跳请求:{}",ldmRpcPackage.getHeader().toString());
                cxt.writeAndFlush(ldmRpcPackage);
            }
        }
    }

    private long now(){
        return System.currentTimeMillis();
    }


}
