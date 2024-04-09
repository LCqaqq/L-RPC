package com.lc.example.provider;

import com.lc.example.common.service.UserService;
import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RegistryConfig;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.model.ServiceMetaInfo;
import com.lc.lrpc.registry.LocalRegistry;
import com.lc.lrpc.registry.Registry;
import com.lc.lrpc.registry.RegistryFactory;
import com.lc.lrpc.server.HttpServer;
import com.lc.lrpc.server.VertxHttpServer;

public class ProviderExample {
    public static void main(String[] args){
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        //注册到服务中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        //启动服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStar(RpcApplication.getRpcConfig().getServerPort());

    }
}
