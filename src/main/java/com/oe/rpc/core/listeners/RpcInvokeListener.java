package com.oe.rpc.core.listeners;

import com.oe.rpc.context.IRpcListener;
import com.oe.rpc.context.RpcEvent;
import com.oe.rpc.core.dispatch.IRpcDispatcher;
import com.oe.rpc.core.dispatch.RpcDispatcher;
import com.oe.rpc.core.events.RpcInvokeEvent;
import com.oe.rpc.core.model.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rpc调用处理器
 */
public class RpcInvokeListener implements IRpcListener<RpcInvokeEvent> {


    private static Logger logger = LoggerFactory.getLogger(RpcInvokeListener.class);

    private static  final IRpcDispatcher rpcDispatcher=new RpcDispatcher();

    /**
     * 1.获取服务列表
     * 2.连接服务
     * 3.服务调用并阻塞等待结果
     * 4.结果回调返回结果
     * @param rpcEvent
     * @return
     */
    @Override
    public Object excute(RpcEvent rpcEvent) {
        Param pa = (Param) rpcEvent.getSource();
        return  rpcDispatcher.dispatch(pa);
    }
}
