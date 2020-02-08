package com.oe.rpc.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class RegisterBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        return LdmRegisterBean.class;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        element.setAttribute("id","ldmRegister");
        String url = element.getAttribute("url");
        String timeout = element.getAttribute("timeout");
        if(StringUtils.hasText(url)){
            builder.addPropertyValue("url",url);
        }
        if(StringUtils.hasText(timeout)){
            builder.addPropertyValue("timeout",timeout);
        }else {
            builder.addPropertyValue("timeout",3000);
        }
    }
}
