package com.oe.rpc.context;

import com.oe.rpc.common.ReflectUtil;
import com.oe.rpc.core.listeners.ExcuteReqListener;
import com.oe.rpc.core.listeners.ExcuteRespListener;
import com.oe.rpc.core.listeners.RpcInvokeListener;

import java.util.HashMap;
import java.util.Map;

public class LdmRpcEventProcessor {

    Map<Class, IRpcListener> listenerMap=new HashMap<>();

    private static  LdmRpcEventProcessor processor=new LdmRpcEventProcessor();

    private LdmRpcEventProcessor(){
        loadDefaultListener();
    }

    private void loadDefaultListener() {
        addListener(new ExcuteReqListener());
        addListener(new RpcInvokeListener());
        addListener(new ExcuteRespListener());
    };

    public static LdmRpcEventProcessor newInstance(){
        if(null==processor){
            processor=new LdmRpcEventProcessor();
        }
        return processor;
    }




    public void addListener(IRpcListener rpcListener){
        Class<?> interfaceT = ReflectUtil.getInterfaceT(rpcListener, 0);
        listenerMap.put(interfaceT,rpcListener);
    }

    public Object publish(RpcEvent rpcEvent){
        IRpcListener iRpcListener = listenerMap.get(rpcEvent.getClass());
        return iRpcListener.excute(rpcEvent);
    }
}
