package com.oe.rpc.exception;

public class LdmRpcException extends RuntimeException{

    public LdmRpcException(String message) {
        super(message);
    }

    public LdmRpcException(Throwable cause) {
        super(cause);
    }
}
