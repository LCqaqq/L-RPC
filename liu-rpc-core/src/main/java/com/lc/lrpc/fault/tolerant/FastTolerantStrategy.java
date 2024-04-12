package com.lc.lrpc.fault.tolerant;

import com.lc.lrpc.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 快速失败
 */
public class FastTolerantStrategy implements com.lc.lrpc.fault.tolerant.TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String,Object> context, Exception e){
        throw new RuntimeException("服务报错",e);
    }
}
