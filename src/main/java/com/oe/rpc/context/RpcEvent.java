package com.oe.rpc.context;

public abstract  class RpcEvent {

    private Object source;

    public RpcEvent(Object object) {
        this.source = object;
    }

    public Object getSource() {
        return source;
    }
}
