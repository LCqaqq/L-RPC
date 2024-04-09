package com.lc.example.provider;

import com.lc.example.common.service.UserService;
import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RegistryConfig;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.constant.RpcConstant;
import com.lc.lrpc.model.ServiceMetaInfo;
import com.lc.lrpc.registry.LocalRegistry;
import com.lc.lrpc.registry.Registry;
import com.lc.lrpc.registry.RegistryFactory;
import com.lc.lrpc.server.HttpServer;
import com.lc.lrpc.server.VertxHttpServer;

import java.util.List;

/**
 * 简单服务提供者
 */
public class EasyProviderExample {
    public static void main(String[] args){
        //RPC框架初始化
        RpcApplication.init();
        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
        //启动服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStar(RpcApplication.getRpcConfig().getServerPort());

    }
}
///**
// * 简单服务提供者
// */
//public class EasyProviderExample {
//    public static void main(String[] args){
//        //RPC框架初始化
//        RpcApplication.init();
//        //注册服务
//        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);
//        //启动服务
//        HttpServer httpServer = new VertxHttpServer();
//        httpServer.doStar(RpcApplication.getRpcConfig().getServerPort());
//
//    }
//}
