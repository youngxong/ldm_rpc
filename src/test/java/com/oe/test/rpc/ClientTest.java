package com.oe.test.rpc;

import com.oe.test.cases.ITestService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by ouyongxiong on 2020/2/5.
 */
public class ClientTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("application-consumer.xml");
        ITestService bean =(ITestService) classPathXmlApplicationContext.getBean("testService");
        System.out.println( bean.test("ddd"));
            try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
