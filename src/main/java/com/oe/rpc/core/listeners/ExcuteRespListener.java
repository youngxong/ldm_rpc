package com.oe.rpc.core.listeners;

import com.oe.rpc.context.IRpcListener;
import com.oe.rpc.context.RpcContext;
import com.oe.rpc.context.RpcEvent;
import com.oe.rpc.core.events.ExcuteRespEvent;
import com.oe.rpc.core.model.RpcCall;
import com.oe.rpc.core.model.RpcInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ouyongxiong on 2020/2/6.
 */
public class ExcuteRespListener implements IRpcListener<ExcuteRespEvent> {

    private static Logger logger = LoggerFactory.getLogger(ExcuteRespListener.class);

    @Override
    public Object excute(RpcEvent rpcEvent) {
        RpcCall rpcCall = (RpcCall) rpcEvent.getSource();
        RpcInvoker invoker = RpcContext.getInstance().getInvoker(rpcCall.getSessionId() + "");
        if(invoker!=null){
            invoker.setRpcCall(rpcCall);
            invoker.complete();
            RpcContext.getInstance().removeInvoker(rpcCall.getSessionId() + "");
        }
        return true;
    }
}
