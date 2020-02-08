package com.oe.rpc.core;

import com.oe.rpc.common.IpUtil;
import com.oe.rpc.core.config.GlobalConfig;
import com.oe.rpc.context.RpcContext;
import com.oe.rpc.core.convert.ObjectBuilder;
import com.oe.rpc.core.model.RpcConsumer;
import com.oe.rpc.core.model.RpcService;
import com.oe.rpc.core.proxy.TargetProxy;
import com.oe.rpc.network.IServer;
import com.oe.rpc.network.NettyServer;
import com.oe.rpc.register.ServiceInfo;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;

public class RpcCore {

    private TargetProxy proxy ;

    public static final RpcCore rpcCore = new RpcCore();

    private RpcCore(){
        proxy= new TargetProxy();
    }

   public static RpcCore newInstance() {
        return rpcCore;
    }

    public synchronized Object proxyConsumer(RpcConsumer consumer) throws Exception{
        Class clazz = consumer.getInterfaceClass();
        if (null == ClassPool.getDefault().getOrNull(clazz.getName())) {
            ClassPool.getDefault().insertClassPath(new ClassClassPath(clazz));
        }
        CtClass ctClass1 = ClassPool.getDefault().getCtClass(clazz.getName());
        CtClass ctClass = proxy.markTargetClass(clazz.getName(), ctClass1);
        RpcContext.getInstance().getContainer().put(consumer.getId(),consumer);
       return ctClass.toClass().newInstance();
    }

    public  synchronized  void exposeService(RpcService rpcService){
        /**
         * 选取本地ip,开启本地服务监听
         */
        String localIpByNetcard = IpUtil.getLocalIpByNetcard();
        int serverPort = GlobalConfig.getInstance().getServerPort();
        String url=localIpByNetcard+":"+serverPort;
        if(RpcContext.getInstance().getServer(url)==null){
            IServer server = new NettyServer(localIpByNetcard,serverPort);
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            RpcContext.getInstance().cacheServer(url,server);
        }
        /**
         * 将服务信息注册进center
         */
        rpcService.setUrl(url);
        ServiceInfo serviceInfo = ObjectBuilder.buildServiceInfo(rpcService);
        RpcContext.getInstance().getRegisterHolder().center().register(serviceInfo);
        RpcContext.getInstance().getContainer().put(rpcService.getServiceName(),rpcService);
    };

}
