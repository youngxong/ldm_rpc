package com.oe.rpc.network;

public abstract class AbstractServer implements IServer{

    public  final static int RUNNING=1;

    public  final static int CLOSE=0;

    public volatile  int state=CLOSE;

    public Boolean isRunning(){
        return state==RUNNING;
    };
}
