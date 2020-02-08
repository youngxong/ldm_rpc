package com.oe.rpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LdmRpcNameSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service",new ServiceBeanDefinitionParser());
        registerBeanDefinitionParser("consumer",new ConsumerBeanDefinitionParser());
        registerBeanDefinitionParser("register",new RegisterBeanDefinitionParser());
    }
}
