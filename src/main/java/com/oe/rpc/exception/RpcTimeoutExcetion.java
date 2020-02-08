package com.oe.rpc.exception;

/**
 * Created by ouyongxiong on 2020/2/6.
 */
public class RpcTimeoutExcetion extends RuntimeException {
    public RpcTimeoutExcetion(String message) {
        super(message);
    }

    public RpcTimeoutExcetion(Throwable cause) {
        super(cause);
    }
}
