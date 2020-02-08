package com.oe.rpc.context;

import com.oe.rpc.core.config.ConsumerConfig;
import com.oe.rpc.core.config.RegisterConfig;
import com.oe.rpc.core.config.ServiceConfig;
import com.oe.rpc.core.model.RpcInvoker;
import com.oe.rpc.network.IClient;
import com.oe.rpc.network.IServer;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentHashMap;

public class RpcContext {

    private RegisterHolder registerHolder;

    private RpcContainer rpcContainer;

    private static ConcurrentHashMap<String, RpcInvoker> invokerMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, IClient> clientMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, IServer> serverMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, ServiceConfig> serviceConfigMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, RegisterConfig> registerConfigMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, ConsumerConfig> consumerConfigMap = new ConcurrentHashMap<>();

    boolean inited=false;

    boolean centerInited=false;

    boolean containerInited=false;


    public static final RpcContext context= new RpcContext();

    private RpcContext(){}

    public static  RpcContext getInstance(){
        return context;
    }

    public synchronized void initContext(){
        if(inited){
            return;
        }
        /**
         * 各种配置的初始化等等--待实现
         */

    }

    public synchronized   void initCenter(String url,int timeout){
        if(centerInited){
            return;
        }
        this.registerHolder=new RegisterHolder(url,timeout);
    }


    public synchronized  void initContainer(ApplicationContext context){
        if(containerInited){
            return;
        }
        this.rpcContainer =new RpcContainer(context);
    }

    public RegisterHolder getRegisterHolder(){
        return  registerHolder;
    }

    public RpcContainer getContainer(){
        return  rpcContainer;
    }


    public  void cacheInvoker(String key,RpcInvoker invoker){
        invokerMap.put(key,invoker);
    }

    public  RpcInvoker getInvoker(String key){
        return  invokerMap.get(key);
    }

    public  void removeInvoker(String key){
        invokerMap.remove(key);
    }

    public  void cacheClient(String key,IClient client){
        clientMap.put(key,client);
    }

    public  IClient getClient(String key){
        return  clientMap.get(key);
    }

    public  void removeClient(String key){
        clientMap.remove(key);
    }

    public   void  cacheServer(String url,IServer server){
        serverMap.put(url,server);
    }

    public   IServer  getServer(String url){
        return  serverMap.get(url);
    }

    public   IServer  removeServer(String url){
        return  serverMap.remove(url);
    }

    public ServiceConfig getServiceConfig(String name){
        return serviceConfigMap.get(name);
    }

    public void cacheServiceConfig(String name,ServiceConfig serviceConfig){
         serviceConfigMap.put(name,serviceConfig);
    }

    public ConsumerConfig getConsumerConfig(String name){
        return consumerConfigMap.get(name);
    }

    public void cacheConsumerConfig(String name,ConsumerConfig consumerConfig){
         consumerConfigMap.put(name,consumerConfig);
    }


}
