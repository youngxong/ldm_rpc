package com.oe.rpc.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by ouyongxiong on 2020/2/4.
 */
public class SessionIdUtil {


    public static synchronized long generateSessionId() {
        long sessionId=(long)Math.random()*100000000;
        String localIP = IpUtil.getLocalIP();
        int hash = localIP.hashCode();
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        long r = random.nextLong();
        sessionId=hash+r+sessionId;
        return  sessionId;
    }
}
