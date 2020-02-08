package com.oe.rpc.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface RpcSerializer {

    byte[] serialize(Object obj) ;

   void serialize(Object obj, OutputStream out) ;

   <T> T deserialize(byte[] bytes, Class<T> tClass) ;


    <T> T deserialize(InputStream in, Class<T> tClass) ;

    Object deserialize(InputStream in) ;

    Object deserialize(byte[] bytes);



}
