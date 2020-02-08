package com.oe.rpc.core.config;

import com.oe.rpc.core.model.RpcInvoker;
import com.oe.rpc.network.IClient;
import com.oe.rpc.network.IServer;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalConfig {

    public static GlobalConfig globalConfig = new GlobalConfig();

    private GlobalConfig(){};

    public static GlobalConfig getInstance(){
        if (null==globalConfig){
            globalConfig=new GlobalConfig();
        }
        return globalConfig;
    }

    private int maxPackageSize=1024*1024;

    private int attackCount=1000;

    private int serverPort=20289;


    public int getMaxPackageSize() {
        return maxPackageSize;
    }

    public void setMaxPackageSize(int maxPackageSize) {
        this.maxPackageSize = maxPackageSize;
    }

    public int getAttackCount() {
        return attackCount;
    }

    public void setAttackCount(int attackCount) {
        this.attackCount = attackCount;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }


}
