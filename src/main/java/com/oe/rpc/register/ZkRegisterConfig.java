package com.oe.rpc.register;

public class ZkRegisterConfig extends AbstractRegisterConfig {

    private String url;

    private int timeOut;

    public ZkRegisterConfig(String url, int timeOut) {
        this.url = url;
        this.timeOut = timeOut;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }
}
