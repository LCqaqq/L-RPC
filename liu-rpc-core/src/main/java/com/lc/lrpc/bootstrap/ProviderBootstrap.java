package com.lc.lrpc.bootstrap;

import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RegistryConfig;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.model.ServiceMetaInfo;
import com.lc.lrpc.model.ServiceRegisterInfo;
import com.lc.lrpc.registry.LocalRegistry;
import com.lc.lrpc.registry.Registry;
import com.lc.lrpc.registry.RegistryFactory;
import com.lc.lrpc.server.HttpServer;
import com.lc.lrpc.server.VertxHttpServer;

import java.util.List;

public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo> serviceRegisterInfoList){
        //RPC框架初始化
        RpcApplication.init();
        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //注册服务
        for (ServiceRegisterInfo serviceRegisterInfo:serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServerName();
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImpClass());
            //注册到服务中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName +"服务注册失败",e);
            }
//        //启动服务
            HttpServer httpServer = new VertxHttpServer();
            httpServer.doStar(RpcApplication.getRpcConfig().getServerPort());

            //启动服务
//        VertxTcpServer vertxTcpServer = new VertxTcpServer();
//        vertxTcpServer.doStar(8088);
        }
    }

}
