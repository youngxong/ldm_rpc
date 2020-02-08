package com.oe.rpc.context;

import com.oe.rpc.core.model.RpcService;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ConcurrentHashMap;

public class RpcContainer {

     ConcurrentHashMap<String, Object> serviceMap=new ConcurrentHashMap<>();

    ApplicationContext applicationContext;

    public RpcContainer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object get(String name){
        Object object=null;
        if(applicationContext!=null){
            object=applicationContext.getBean(name);
        }else{
            object=serviceMap.get(name);
        }
        return object;
    };

    public void put(String name,Object obj){
        if(applicationContext==null){
            serviceMap.put(name,obj);
        }
    }





}
