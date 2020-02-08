package com.oe.rpc.core.convert;

import com.oe.rpc.common.EnumHeaderType;
import com.oe.rpc.common.EnumSerializeType;
import com.oe.rpc.common.SessionIdUtil;
import com.oe.rpc.context.RpcContext;
import com.oe.rpc.core.model.Param;
import com.oe.rpc.core.model.RpcCall;
import com.oe.rpc.core.model.RpcConsumer;
import com.oe.rpc.core.model.RpcService;
import com.oe.rpc.network.Header;
import com.oe.rpc.network.LdmRpcPackage;
import com.oe.rpc.register.ServiceInfo;
import com.oe.rpc.serialization.RpcSerializer;
import com.oe.rpc.serialization.RpcSerializerFactory;
import com.oe.rpc.spring.LdmRegisterBean;
import com.sun.org.apache.xml.internal.serialize.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

public class ObjectBuilder {

    public static ServiceInfo buildServiceInfo(RpcService service){
        ServiceInfo info = new ServiceInfo();
        Class interfaceClass = service.getInterfaceClass();
        Method[] methods = interfaceClass.getDeclaredMethods();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Method method : methods) {
            String name = method.getName();
            sb.append(name+",");
        }
        sb.delete(sb.length()-1,sb.length());
        sb.append("]");
        info.setUrl(service.getUrl());
        info.setMethods(sb.toString());
        info.setServiceName(service.getServiceName());
        info.setVersion(service.getVersion());
        info.setWeights(service.getWeights());
        return info;
    }

    public static RpcCall buildRpcCall(LdmRpcPackage ldmRpcPackage){
        Header header = ldmRpcPackage.getHeader();
        RpcSerializer rpcSerializer = RpcSerializerFactory.buildSerializer(header.getSerializeType());
        byte[] body = ldmRpcPackage.getBody();
        return rpcSerializer.deserialize(body,RpcCall.class);
    }

    public static RpcCall buildRpcCall(Param param){
        RpcCall rpcCall = new RpcCall();

        String method = param.getMethod();
        int i = method.lastIndexOf(".");
        String serviceName = method.substring(0, i);
        String methodName = method.substring(i + 1, method.length());
        rpcCall.setSessionId(SessionIdUtil.generateSessionId());
        rpcCall.setServiceName(serviceName);
        rpcCall.setMethod(methodName);
        rpcCall.setParams(param.getParams());
        rpcCall.setType(EnumHeaderType.REQUEST.getCode());
        rpcCall.setSerializeType(EnumSerializeType.JDK.getCode());
        return rpcCall;
    }

    public static LdmRpcPackage buildLdmRpcPackage(RpcCall call,int type){
        LdmRpcPackage rpcPackage = new LdmRpcPackage();
        Header header=new Header();
        rpcPackage.setHeader(header);
        header.setType(EnumHeaderType.REQUEST.getCode());
        header.setSessionId(call.getSessionId());
        header.setSerializeType(type);
        RpcSerializer rpcSerializer = RpcSerializerFactory.buildSerializer(type);
        byte[] bytes = rpcSerializer.serialize(call);
        rpcPackage.setBody(bytes);
        header.setLength(bytes.length);
        return rpcPackage;
    }

    public static LdmRpcPackage buildHeartBeatReq(){
        LdmRpcPackage rpcPackage = new LdmRpcPackage();
        Header header=new Header();
        rpcPackage.setHeader(header);
        header.setSessionId(SessionIdUtil.generateSessionId());
        header.setType(EnumHeaderType.HEARTBEAT.getCode());
        header.setSerializeType(EnumSerializeType.JDK.getCode());
        header.setLength(0);
        return rpcPackage;
    }


    public static String buildPackageInfo(LdmRpcPackage ldmRpcPackage){
        Header header = ldmRpcPackage.getHeader();
        RpcSerializer rpcSerializer = RpcSerializerFactory.buildSerializer(header.getSerializeType());
        RpcCall rp = rpcSerializer.deserialize(ldmRpcPackage.getBody(), RpcCall.class);
        String ret=header.toString()+"-"+rp.toString();
        return ret;
    }
}
