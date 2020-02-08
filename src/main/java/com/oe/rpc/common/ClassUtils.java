package com.oe.rpc.common;

import com.oe.rpc.exception.LdmRpcException;

/**
 * Created by ouyongxiong on 2020/2/5.
 */
public class ClassUtils {

    public static String getClassName(String fullName){
        int i = fullName.lastIndexOf(".");
        if(i==-1){
            return fullName;
        }
        String className = fullName.substring(i + 1, fullName.length());
        char[] chars = className.toCharArray();
        if(chars[0]>=97){
            chars[0]-=32;
        }
        return String.valueOf(chars);
    }

    public static Class classForName(String fullName){
        Class c=null;
        try {
            c=Class.forName(fullName);
        } catch (ClassNotFoundException e) {
            throw new LdmRpcException(e);
        }
        return c;
    }
}
