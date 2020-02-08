package com.oe.rpc.core.model;

import com.oe.rpc.core.config.ServiceConfig;

public class RpcService extends ServiceConfig{

    private String serviceName;

    private Class interfaceClass;

    private Object ref;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Object getRef() {
        return ref;
    }

    public void setRef(Object ref) {
        this.ref = ref;
    }
}
