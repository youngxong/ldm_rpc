package com.oe.rpc.register;

import java.util.List;

public abstract class AbstractRegisterCenter<T> {

    public abstract void init(AbstractRegisterConfig config);

    public abstract void connect();

    public abstract void createRoot();

    public abstract  void register(T t);

    public abstract T get(String serviceName);

    public abstract List<T> list(String serviceName);

    public abstract void close();
}
