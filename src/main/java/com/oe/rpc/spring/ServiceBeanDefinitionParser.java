package com.oe.rpc.spring;

import com.oe.rpc.common.ClassUtils;
import com.oe.rpc.exception.LdmRpcException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class ServiceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {


    @Override
    protected Class<?> getBeanClass(Element element) {
        return LdmServiceBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String className = element.getAttribute("interface");
        Class<?> aClass= ClassUtils.classForName(className);
        element.setAttribute("id",className);
        builder.addPropertyValue("serviceName",className);
        builder.addPropertyValue("interfaceClass",aClass);
        String ref = element.getAttribute("ref");
        builder.addPropertyReference("ref",ref);
        String timeout = element.getAttribute("timeout");
        if(StringUtils.hasText(timeout)){
            builder.addPropertyValue("timeout",timeout);
        }else {
            builder.addPropertyValue("timeout",3000);
        }
        String version = element.getAttribute("version");
        if(StringUtils.hasText(version)){
            builder.addPropertyValue("version",version);
        }else {
            builder.addPropertyValue("version",className);
        }
        String weights = element.getAttribute("weights");
        if(StringUtils.hasText(weights)){
            builder.addPropertyValue("weights",weights);
        }else {
            builder.addPropertyValue("weights",1);
        }

    }
}
