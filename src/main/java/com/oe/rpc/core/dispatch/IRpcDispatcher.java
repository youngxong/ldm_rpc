package com.oe.rpc.core.dispatch;

import com.oe.rpc.core.model.Param;
import com.oe.rpc.core.model.RpcCall;

public interface IRpcDispatcher {

    Object dispatch(Param param);

}
