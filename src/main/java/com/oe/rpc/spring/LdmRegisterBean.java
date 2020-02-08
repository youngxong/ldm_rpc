package com.oe.rpc.spring;

import com.oe.rpc.core.config.RegisterConfig;
import com.oe.rpc.context.RpcContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
public class LdmRegisterBean extends RegisterConfig implements InitializingBean, ApplicationContextAware, DisposableBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        initRegister();
    }

    private void initRegister() {
        RpcContext.getInstance().initCenter(this.getUrl(),this.getTimeout());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RpcContext.getInstance().initContainer(applicationContext);
    }


    @Override
    public void destroy() throws Exception {
        RpcContext.getInstance().getRegisterHolder().center().close();
    }
}
