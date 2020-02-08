package com.oe.rpc.common;

/**
 * Created by ouyongxiong on 2020/2/5.
 */
public enum EnumHeaderType {

    REQUEST(0,"REQUEST"),
    HEARTBEAT(2,"HEARTBEAT"),
    RESPONSE(1,"RESPONSE");

    EnumHeaderType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public  static  EnumHeaderType find(byte code){
        EnumHeaderType[] values = EnumHeaderType.values();
        for (int i = 0; i < values.length; i++) {
            EnumHeaderType value = values[i];
            if(value.code==code){
                return value;
            }
        }
        return null;
    }

    private  int  code;

    private String desc;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
