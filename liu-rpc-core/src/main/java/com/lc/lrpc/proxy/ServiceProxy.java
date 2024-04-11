package com.lc.lrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.constant.RpcConstant;
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
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
//        Serializer serializer = new JdkSerializer();
        //使用工厂+读取配置获取实现类
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        //发请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest =RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            //序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);

            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscover(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }

            //负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("methodName",rpcRequest.getMethodName());

            ServiceMetaInfo selectServiceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);

            //发送请求
            RpcResponse rpcResponse = VertxTcpClient.doRequest(rpcRequest,selectServiceMetaInfo);
            return rpcResponse.getData();
        } catch (Exception e){
           throw new RuntimeException("调用失败");
        }
    }
}
