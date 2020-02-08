package com.oe.rpc.core.model;

import com.oe.rpc.common.EnumSerializeType;
import com.oe.rpc.core.config.ConsumerConfig;
import com.oe.rpc.context.RpcContext;
import com.oe.rpc.core.convert.ObjectBuilder;
import com.oe.rpc.exception.RpcTimeoutExcetion;
import com.oe.rpc.network.IClient;
import com.oe.rpc.network.LdmRpcPackage;
import com.oe.rpc.network.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RpcInvoker {

    private static Logger logger = LoggerFactory.getLogger(RpcInvoker.class);

    private IClient client;

    private RpcCall rpcCall;

    private ConsumerConfig consumerConfig;

    protected Lock lock = new ReentrantLock();

    protected Condition condition=lock.newCondition();


    public RpcInvoker(IClient client, RpcCall rpcCall) {
        this.client = client;
        this.rpcCall = rpcCall;
        this.consumerConfig = RpcContext.getInstance().getConsumerConfig(rpcCall.getServiceName());
    }


    public void invoke(){
        LdmRpcPackage ldmRpcPackage = ObjectBuilder.buildLdmRpcPackage(rpcCall, EnumSerializeType.JDK.getCode());
        client.send(ldmRpcPackage);
        RpcContext.getInstance().cacheInvoker(rpcCall.getSessionId()+"",this);
        hold(consumerConfig.getTimeout());
    }

    public void complete(){
        lock.lock();
        try {
            condition.signalAll();
        }catch (Exception e){
            logger.error("rpc invoke excetion:{}",e);
        }finally {
            lock.unlock();
        }

    }

    public void hold(long timeout){
        lock.lock();
        try {
            if (!condition.await(timeout, TimeUnit.MILLISECONDS)) {
                rpcCall.setException(new RpcTimeoutExcetion("rpc invoke timeout!!!"));
                return;
            }
        }catch (Exception e){
            logger.error("rpc invoke excetion:{}",e);
        }finally {
            lock.unlock();
        }
    }


    public RpcCall getRpcCall() {
        return rpcCall;
    }

    public void setRpcCall(RpcCall rpcCall) {
        this.rpcCall = rpcCall;
    }
}
