package com.lc.lrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.constant.RpcConstant;
import com.lc.lrpc.fault.retry.RetryStrategy;
import com.lc.lrpc.fault.retry.RetryStrategyFactory;
import com.lc.lrpc.fault.tolerant.TolerantStrategy;
import com.lc.lrpc.fault.tolerant.TolerantStrategyFactory;
import com.lc.lrpc.loadbalance.LoadBalancer;
import com.lc.lrpc.loadbalance.LoadBalancerFactory;
import com.lc.lrpc.model.RpcRequest;
import com.lc.lrpc.model.RpcResponse;
import com.lc.lrpc.model.ServiceMetaInfo;
import com.lc.lrpc.protocol.*;
import com.lc.lrpc.registry.Registry;
import com.lc.lrpc.registry.RegistryFactory;
import com.lc.lrpc.serializer.JdkSerializer;
import com.lc.lrpc.serializer.Serializer;
import com.lc.lrpc.serializer.SerializerFactory;
import com.lc.lrpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 自动代理
 */
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        // 将调用方法名（请求路径）作为负载均衡参数
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
//            // http 请求
//            // 指定序列化器
            Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            RpcResponse rpcResponse = doHttpRequest(selectedServiceMetaInfo, bodyBytes);
        // rpc 请求
        // 使用重试机制
//        RpcResponse rpcResponse;
//        try {
//            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
//            rpcResponse = retryStrategy.doRetry(() ->
//                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
//            );
//        } catch (Exception e) {
//            // 容错机制
//            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
//            rpcResponse = tolerantStrategy.doTolerant(null, e);
//        }
        return rpcResponse.getData();
    }

    /**
     * 发送 HTTP 请求
     *
     * @param selectedServiceMetaInfo
     * @param bodyBytes
     * @return
     * @throws IOException
     */
    private static RpcResponse doHttpRequest(ServiceMetaInfo selectedServiceMetaInfo, byte[] bodyBytes) throws IOException {
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        // 发送 HTTP 请求
        try (HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
                .body(bodyBytes)
                .execute()) {
            byte[] result = httpResponse.bodyBytes();
            // 反序列化
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse;
        }
    }
}