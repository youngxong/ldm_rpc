package com.oe.rpc.network;

import com.oe.rpc.core.model.RpcCall;

public class LdmRpcPackage {

    private Header header;

    private byte[] body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
