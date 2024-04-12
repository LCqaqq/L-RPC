package com.lc.lrpc.fault.tolerant;

import com.lc.lrpc.model.RpcResponse;
import com.lc.lrpc.model.ServiceMetaInfo;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 容错机制
 */
public interface TolerantStrategy{
    /**
     * 选择容错机制
     * @param
     * @return
     * @throws Exception
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e) ;
}
