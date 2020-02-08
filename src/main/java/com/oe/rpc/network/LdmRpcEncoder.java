package com.oe.rpc.network;

import com.oe.rpc.core.convert.ObjectBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LdmRpcEncoder extends MessageToByteEncoder<LdmRpcPackage> {

    private static Logger logger = LoggerFactory.getLogger(LdmRpcEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, LdmRpcPackage msg, ByteBuf out) throws Exception {
        if(null!=msg){
            Header header = msg.getHeader();
            out.writeLong(header.getSessionId());
            out.writeInt(header.getType());
            out.writeInt(header.getSerializeType());
            out.writeInt(header.getLength());
            if(msg.getBody()!=null&&msg.getBody().length>0){
                out.writeBytes(msg.getBody());
            }
        }
    }
}
