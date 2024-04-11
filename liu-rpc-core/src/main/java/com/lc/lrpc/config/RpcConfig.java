package com.lc.lrpc.config;

import com.lc.lrpc.loadbalance.LoadBalancer;
import com.lc.lrpc.loadbalance.LoadBalancerKeys;
import com.lc.lrpc.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "liu-rpc";
    /**
     * 版本号
     */
    private String version = "1.0";
    /**
     * 主机名称
     */
    private String serverHost = "localhost";
    /**
     * 服务器端口号
     */
    private Integer serverPort = 8088;
    /**
     * 模拟调用
     */
    private boolean mock = false;
    /**
     * 序列化器
     */
    private String Serializer = SerializerKeys.JDK;
    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();
    /**
     * 负载均衡器配置
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
}
