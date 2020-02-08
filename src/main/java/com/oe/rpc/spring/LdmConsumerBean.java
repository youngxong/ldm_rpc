package com.oe.rpc.spring;

import com.oe.rpc.context.RpcContext;
import com.oe.rpc.core.RpcCore;
import com.oe.rpc.core.model.RpcConsumer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

public class LdmConsumerBean extends RpcConsumer implements FactoryBean, DisposableBean {

    @Override
    public Object getObject() throws Exception {
        Object o = RpcCore.newInstance().proxyConsumer(this);
        RpcContext.getInstance().cacheConsumerConfig(this.getInterfaceClass().getName(),this);
        return o;
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {

    }
}
