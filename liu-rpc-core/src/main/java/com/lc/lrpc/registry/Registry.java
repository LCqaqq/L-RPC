package com.lc.lrpc.registry;

import com.lc.lrpc.config.RegistryConfig;
import com.lc.lrpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * 注册中心
 */
public interface Registry {
    /**
     * 初始化
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     */
    void register(ServiceMetaInfo ServiceMetaInfo) throws Exception;
    /**
     * 注销服务
     */
    void unRegister(ServiceMetaInfo ServiceMetaInfo);
    /**
     * 服务发现
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);
    /**
     * 服务销毁
     */
    void destroy();

    /**
     * 心跳检测
     */
    void heartBeat();
    /**
     * 监听（消费端）
     */
    void watch(String serviceNodeKey);
}
