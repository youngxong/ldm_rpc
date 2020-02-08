package com.oe.rpc.core.listeners;

import com.oe.rpc.context.IRpcListener;
import com.oe.rpc.context.RpcContext;
import com.oe.rpc.context.RpcEvent;
import com.oe.rpc.core.events.ExcuteReqEvent;
import com.oe.rpc.core.model.RpcCall;
import com.oe.rpc.network.LdmRpcPackage;
import com.oe.rpc.serialization.RpcSerializer;
import com.oe.rpc.serialization.RpcSerializerFactory;
import com.oe.rpc.spring.LdmServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ExcuteReqListener implements IRpcListener<ExcuteReqEvent> {

    private static Logger logger = LoggerFactory.getLogger(ExcuteReqListener.class);


    @Override
    public Object excute(RpcEvent rpcEvent) {
        logger.info("开始执行本地方法处理！");
        RpcCall rpcCall = (RpcCall) rpcEvent.getSource();
        LdmServiceBean bean = (LdmServiceBean)RpcContext.getInstance().getContainer().get(rpcCall.getServiceName());
        Object ref = bean.getRef();
        Method m=null;
        Method[] declaredMethods = ref.getClass().getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            Method declaredMethod = declaredMethods[i];
            logger.info("执行方法名：{}",declaredMethod.getName());
            if(rpcCall.getMethod().equals(declaredMethod.getName())){
                m=declaredMethod;
            }
        }
        try {
            Object invoke = m.invoke(ref, rpcCall.getParams());
            rpcCall.setResult(invoke);
        } catch (Exception e) {
            logger.error("执行本地方法异常：{}",e);
            rpcCall.setException(e);
        }
        logger.info("结束执行本地方法处理！");
        return rpcCall;

    }
}
