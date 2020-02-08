package com.oe.rpc.spring;

import com.oe.rpc.common.ClassUtils;
import com.oe.rpc.exception.LdmRpcException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ConsumerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return LdmConsumerBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String anInterface = element.getAttribute("interface");
        Class<?> aClass = ClassUtils.classForName(anInterface);
        builder.addPropertyValue("interfaceClass",aClass);
        String id = element.getAttribute("id");
        if(StringUtils.hasText(id)){
            element.setAttribute("id",id);
        }else {
            element.setAttribute("id",ClassUtils.getClassName(anInterface));
        }
        String timeout = element.getAttribute("timeout");
        if(StringUtils.hasText(timeout)){
            builder.addPropertyValue("timeout",timeout);
        }else {
            builder.addPropertyValue("timeout","3000");
        }

    }
}
