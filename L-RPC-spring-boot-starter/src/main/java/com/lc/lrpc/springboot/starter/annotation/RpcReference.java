package com.lc.lrpc.springboot.starter.annotation;

import com.lc.lrpc.constant.RpcConstant;
import com.lc.lrpc.fault.retry.RetryStrategyKeys;
import com.lc.lrpc.fault.tolerant.TolerantStrategyKeys;
import com.lc.lrpc.loadbalance.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者注解（用于注入服务）
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {
    /**
     * 接口类型
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 默认版本号
     * @return
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;
    /**
     * 负载均衡
     * @return
     */
    String loadBalance() default LoadBalancerKeys.RANDOM;
    /**
     * 重试策略
     * @return
     */
    String retryStrategy() default RetryStrategyKeys.NO;
    /**
     * 容忍策略
     * @return
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAST;
    /**
     * 模拟调用
     * @return
     */
    boolean mock() default false;
}
