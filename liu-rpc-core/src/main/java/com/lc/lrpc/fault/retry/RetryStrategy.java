package com.lc.lrpc.fault.retry;

import com.lc.lrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 失败重试
 */
public interface RetryStrategy {
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
