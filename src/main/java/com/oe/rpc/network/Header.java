package com.oe.rpc.network;

public class Header {


    private long sessionId;//唯一标识

    private int type;//0为请求，1为响应,2为心跳

    private int serializeType;//序列化方式

    private int length;//body数据的长度


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public int getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(int serializeType) {
        this.serializeType = serializeType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Header{" +
                "sessionId=" + sessionId +
                ", type=" + type +
                ", serializeType=" + serializeType +
                ", length=" + length +
                '}';
    }
}
