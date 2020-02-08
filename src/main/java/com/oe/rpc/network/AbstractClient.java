package com.oe.rpc.network;

public abstract class AbstractClient implements IClient{

    public final static int ACTIVE=1;

    public final static int CLOSE=0;

    public int state=CLOSE;


    public boolean isActive(){
        return state==ACTIVE;
    }
}
