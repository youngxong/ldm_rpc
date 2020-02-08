package com.oe.rpc.serialization;

import com.oe.rpc.exception.LdmRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class DefaultSerializer implements RpcSerializer {

    private static Logger logger = LoggerFactory.getLogger(DefaultSerializer.class);

    public byte[] serialize(Object obj)  {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try{
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }catch (Exception e){
            logger.error("序列化异常：{}",e);
            throw new LdmRpcException("序列化异常!!!");
        }finally {
            try {
                if(objectOutputStream!=null){
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                logger.error("序列化异常：{}",e);
                throw new LdmRpcException("序列化异常!!!");
            }
        }
    }

    public void serialize(Object obj, OutputStream out) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
        } catch (IOException e) {
            logger.error("序列化异常：{}",e);
            throw new LdmRpcException("序列化异常!!!");
        }

    }

    public <T> T deserialize(byte[] bytes, Class<T> tClass) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return  (T)objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("序列化异常：{}",e);
            throw new LdmRpcException("序列化异常!!!");
        }finally {
            if(objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    logger.error("序列化异常：{}",e);
                    throw new LdmRpcException("序列化异常!!!");
                }
            }
        }
    }

    public <T> T deserialize(InputStream in, Class<T> tClass) {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(in);
            return (T)objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("序列化异常：{}",e);
            throw new LdmRpcException("序列化异常!!!");
        }

    }

    public Object deserialize(InputStream in) {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(in);
            return objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("序列化异常：{}",e);
            throw new LdmRpcException("序列化异常!!!");
        }

    }

    public Object deserialize(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("序列化异常：{}",e);
            throw new LdmRpcException("序列化异常!!!");
        }finally {
            if(objectInputStream!=null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    logger.error("序列化异常：{}",e);
                    throw new LdmRpcException("序列化异常!!!");
                }
            }
        }
    }
}
