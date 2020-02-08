package com.oe.rpc.common;

public enum EnumSerializeType {

    JDK(0,"jdk"),
    HESSIAN(1,"hessian");

    EnumSerializeType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public  static  EnumSerializeType find(int code){
        EnumSerializeType[] values = EnumSerializeType.values();
        for (int i = 0; i < values.length; i++) {
            EnumSerializeType value = values[i];
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
