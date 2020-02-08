package com.oe.rpc.network;

import com.oe.rpc.common.EnumHeaderType;
import com.oe.rpc.core.convert.ObjectBuilder;
import com.oe.rpc.core.events.ExcuteReqEvent;
import com.oe.rpc.context.LdmRpcEventProcessor;
import com.oe.rpc.core.events.ExcuteRespEvent;
import com.oe.rpc.core.model.RpcCall;
import com.oe.rpc.serialization.RpcSerializer;
import com.oe.rpc.serialization.RpcSerializerFactory;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServiceHandler extends ChannelHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(RpcServiceHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LdmRpcPackage ldmRpcPackage= (LdmRpcPackage)msg;
        Header header = ldmRpcPackage.getHeader();
        LdmRpcEventProcessor processor=LdmRpcEventProcessor.newInstance();
        if(EnumHeaderType.REQUEST.getCode()==header.getType()){
            RpcCall rpcCall = ObjectBuilder.buildRpcCall(ldmRpcPackage);
            logger.info("接收请求:{}-{}",header.toString(),ObjectBuilder.buildRpcCall(ldmRpcPackage).toString());
            RpcCall result =(RpcCall) processor.publish(new ExcuteReqEvent(rpcCall));
            resp(ctx,result);
        }else if(EnumHeaderType.RESPONSE.getCode()==header.getType()){
            RpcCall rpcCall = ObjectBuilder.buildRpcCall(ldmRpcPackage);
            logger.info("接收响应:{}-{}",header.toString(),rpcCall.toString());
            processor.publish(new ExcuteRespEvent(rpcCall));
        }else if(EnumHeaderType.HEARTBEAT.getCode()==header.getType()){
            logger.info("接收到心跳信息:{}",header.toString());
            respHeart(ctx,ldmRpcPackage);
        }

    }

    private void respHeart(ChannelHandlerContext ctx,LdmRpcPackage ldmRpcPackage) {
        logger.info("响应心跳信息:{}",ldmRpcPackage.getHeader().toString());
        ctx.writeAndFlush(ldmRpcPackage);
    }

    private void resp(ChannelHandlerContext ctx,RpcCall result) {
        LdmRpcPackage ldmRpcPackage = new LdmRpcPackage();
        result.setType(EnumHeaderType.RESPONSE.getCode());
        RpcSerializer rpcSerializer = RpcSerializerFactory.buildSerializer(result.getSerializeType());
        byte[] bytes = rpcSerializer.serialize(result);
        ldmRpcPackage.setBody(bytes);
        Header header = new Header();
        ldmRpcPackage.setHeader(header);
        header.setSessionId(result.getSessionId());
        header.setType(EnumHeaderType.RESPONSE.getCode());
        header.setSerializeType(result.getSerializeType());
        header.setLength(bytes.length);
        logger.info("响应请求:{}-{}",header.toString(),result.toString());
        ctx.writeAndFlush(ldmRpcPackage);
    }


}
