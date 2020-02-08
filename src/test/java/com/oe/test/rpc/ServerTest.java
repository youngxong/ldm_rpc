package com.oe.test.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by ouyongxiong on 2020/2/5.
 */
public class ServerTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("application-services.xml");
        System.out.println("服务端启动啦");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
