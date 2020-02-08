package com.oe.rpc.network;

import com.oe.rpc.core.model.RpcCall;
import com.oe.rpc.serialization.DefaultSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdmRpcDecoder extends FixedLengthFrameDecoder {

    private static Logger logger = LoggerFactory.getLogger(LdmRpcDecoder.class);


    public LdmRpcDecoder(int length) {
        super(length);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        LdmRpcPackage rpcPackage = new LdmRpcPackage();
        Header header = new Header();
        rpcPackage.setHeader(header);
        header.setSessionId(frame.readLong());
        header.setType(frame.readInt());
        header.setSerializeType(frame.readInt());
        header.setLength(frame.readInt());
        if(header.getLength()>0){
            ByteBuf byteBuf = in.readBytes(header.getLength());
            rpcPackage.setBody(byteBuf.array());
        }
        return rpcPackage;
    }
}
