package com.oe.rpc.spring;

import com.oe.rpc.core.RpcCore;
import com.oe.rpc.core.model.RpcService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class LdmServiceBean extends RpcService implements InitializingBean , DisposableBean {


    @Override
    public void afterPropertiesSet() throws Exception {
        RpcCore core =  RpcCore.newInstance();
        core.exposeService(this);
    }

    @Override
    public void destroy() throws Exception {

    }
}
