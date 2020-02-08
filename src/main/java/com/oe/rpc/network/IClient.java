package com.oe.rpc.network;

public interface IClient extends Runnable {

    void connect();

    void send(LdmRpcPackage ldmRpcPackage);

    boolean isActive();

    void close();
}
