package com.oe.rpc.context;

public interface IRpcListener<T extends RpcEvent>  {
    Object excute(RpcEvent rpcEvent);
}
