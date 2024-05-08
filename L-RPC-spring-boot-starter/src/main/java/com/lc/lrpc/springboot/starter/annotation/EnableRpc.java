package com.lc.lrpc.springboot.starter.annotation;

import com.lc.lrpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import com.lc.lrpc.springboot.starter.bootstrap.RpcInitBootstrap;
import com.lc.lrpc.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用rpc注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcConsumerBootstrap.class, RpcProviderBootstrap.class})
public @interface EnableRpc {
    /**
     * 需要启动的server
     */
    boolean needServer() default true;
}
