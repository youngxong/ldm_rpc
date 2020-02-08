package com.oe.rpc.serialization;

import com.oe.rpc.common.EnumSerializeType;
import com.sun.corba.se.impl.naming.cosnaming.NamingUtils;

import java.util.HashMap;
import java.util.Map;

public class RpcSerializerFactory {

    public static RpcSerializerFactory factory = new RpcSerializerFactory();

    private RpcSerializerFactory(){
    }


    private static Map<Integer,RpcSerializer> map = new HashMap<>();

    public static RpcSerializer getSerializer(String name){
        return factory.getSerializer(name);
    }

    private RpcSerializer get(String name){
        return map.get(name);
    }

    public static  RpcSerializer buildSerializer(int way){
        if (map.get(way)!= null){
            return map.get(way);
        }
        RpcSerializer rpcSerializer=null;
       if(EnumSerializeType.JDK.getCode()==way){
           rpcSerializer=new DefaultSerializer();
       }
        map.put(way,rpcSerializer);
       return rpcSerializer;
    };

}
