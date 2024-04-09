package com.lc.lrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.constant.RpcConstant;
import com.lc.lrpc.model.RpcRequest;
import com.lc.lrpc.model.RpcResponse;
import com.lc.lrpc.model.ServiceMetaInfo;
import com.lc.lrpc.registry.Registry;
import com.lc.lrpc.registry.RegistryFactory;
import com.lc.lrpc.serializer.JdkSerializer;
import com.lc.lrpc.serializer.Serializer;
import com.lc.lrpc.serializer.SerializerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 自动代理
 */
public class ServiceProxy implements InvocationHandler {

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
                .parameterType(method.getParameterTypes())
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

            ServiceMetaInfo selectServiceMetaInfo = serviceMetaInfoList.get(0);

            //发送请求
            try(HttpResponse httpResponse = HttpRequest.post(selectServiceMetaInfo.getServiceAddress()).body(bodyBytes).execute()) {
                byte[] result = httpResponse.bodyBytes();
                //反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
