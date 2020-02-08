package com.oe.rpc.register;

import java.io.Serializable;

public class ServiceInfo implements Serializable {


    private static final long serialVersionUID = 3815667120934320259L;

    private String serviceName;

    private String methods;

    private String url;

    private String version;

    private int weights;


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getWeights() {
        return weights;
    }

    public void setWeights(int weights) {
        this.weights = weights;
    }
}
