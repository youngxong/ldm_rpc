package com.oe.rpc.core.model;

import java.io.Serializable;
import java.util.Arrays;

public class RpcCall implements Serializable {

    //序列化ID
    private static final long serialVersionUID = -5809782578272943979L;

    private String serviceName;

    private String method;

    private long sessionId;

    private Object[] params;//请求参数

    private int serializeType;

    private int type;//0是请求，1是响应

    private Object result;//响应对象

    private Exception exception;//

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public int getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(int serializeType) {
        this.serializeType = serializeType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "RpcCall{" +
                "serviceName='" + serviceName + '\'' +
                ", method='" + method + '\'' +
                ", sessionId=" + sessionId +
                ", params=" + Arrays.toString(params) +
                ", serializeType=" + serializeType +
                ", type=" + type +
                ", result=" + result +
                ", exception=" + exception +
                '}';
    }
}
