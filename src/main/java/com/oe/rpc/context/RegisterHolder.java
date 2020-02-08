package com.oe.rpc.context;

import com.oe.rpc.register.*;

public class RegisterHolder {

    private AbstractRegisterConfig config;

    private AbstractRegisterCenter<ServiceInfo> center;

    public RegisterHolder(String url,int timeout) {
        String[] split = url.split(":");
        String protocal=split[0];
        String centerUrl=split[1];
        if("zookeeper".equals(protocal)){
            config=new ZkRegisterConfig(centerUrl,timeout);
            center=new ZkRegister();
            center.init(config);
        }
    }

    public AbstractRegisterCenter<ServiceInfo> center(){
        if(this.center==null){
            throw new RuntimeException("RegisterCenter is not  inited!");
        }
        return this.center;
    }




}
