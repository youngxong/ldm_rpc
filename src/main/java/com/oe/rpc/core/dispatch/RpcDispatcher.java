package com.oe.rpc.core.dispatch;

import com.oe.rpc.context.RpcContext;
import com.oe.rpc.core.convert.ObjectBuilder;
import com.oe.rpc.core.model.Param;
import com.oe.rpc.core.model.RpcCall;
import com.oe.rpc.core.model.RpcInvoker;
import com.oe.rpc.exception.LdmRpcException;
import com.oe.rpc.exception.RpcTimeoutExcetion;
import com.oe.rpc.network.IClient;
import com.oe.rpc.network.NettyClient;
import com.oe.rpc.register.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class RpcDispatcher implements IRpcDispatcher {

    private static Logger logger = LoggerFactory.getLogger(RpcDispatcher.class);


    @Override
    public Object dispatch(Param param) {
        List<ServiceInfo> services = getServices(param.getMethod());
        IClient client = getClient(services);
        RpcCall rpcCall = ObjectBuilder.buildRpcCall(param);
        RpcInvoker invoker = new RpcInvoker(client,rpcCall);
        invoker.invoke();
        RpcCall ret = invoker.getRpcCall();
        if(ret.getException()!=null){
            try {
                if(ret.getException() instanceof RpcTimeoutExcetion){
                    RpcContext.getInstance().removeInvoker(ret.getSessionId()+"");
                }
                throw ret.getException();
            } catch (Exception e) {
                logger.error("rpc 调用异常:{}",e);
                throw new LdmRpcException("rpc 调用异常!!!");
            }
        }
        return ret.getResult();
    }

    private IClient getClient(List<ServiceInfo> services) {
        IClient currentClient=null;
        for (ServiceInfo service : services) {
            IClient client = RpcContext.getInstance().getClient(service.getUrl());
            if(null==client || !client.isActive()){
                String[] url = service.getUrl().split(":");
                client=new NettyClient(url[0],Integer.parseInt(url[1]));
                //加上等待客户端连接等待和重连接的逻辑
                int connectTimes=3;
                int waitTimes=5;
                while(!client.isActive() && connectTimes>0){
                    client.connect();
                        while (!client.isActive() && waitTimes>0){
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        waitTimes--;
                    }
                    if(client.isActive()){
                        break;
                    }
                    connectTimes--;
                    waitTimes=5;
                }
                if(client.isActive()){
                    RpcContext.getInstance().cacheClient(service.getUrl(),client);
                    currentClient=client;
                    break;
                }else {
                    logger.info("cannot connect to server:{}",service.getUrl());
                }

            }else {
                currentClient=client;
                break;
            }
        }
        if(currentClient==null){
            throw new LdmRpcException("cannot not find available service:"+services.get(0).getServiceName());
        }
        return currentClient;
    }

    private List<ServiceInfo> getServices(String method) {
        int i = method.lastIndexOf(".");
        String serviceName = method.substring(0, i);
        List<ServiceInfo> list = RpcContext.getInstance().getRegisterHolder().center().list(serviceName);
        return list;
    }
}
