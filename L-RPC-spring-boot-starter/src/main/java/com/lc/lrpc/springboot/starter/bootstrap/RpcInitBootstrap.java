package com.lc.lrpc.springboot.starter.bootstrap;

import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.server.HttpServer;
import com.lc.lrpc.server.VertxHttpServer;
import com.lc.lrpc.springboot.starter.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
//    /**
//     *  Spring初始化时执行，初始化RPC框架
//     * @param importingClassMetadata
//     * @param registry
//     */
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
//                                        BeanDefinitionRegistry registry) {
//        //获取EnableRpc注解的属性值
//        boolean needService = (boolean) importingClassMetadata
//                .getAllAnnotationAttributes(EnableRpc.class.getName()).getFirst("needServer");
//        //初始化
//        RpcApplication.init();
//        //全局配置
//        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
//
//        //启动服务
//        if (needService){
//            HttpServer httpServer = new VertxHttpServer();
//            httpServer.doStar(rpcConfig.getServerPort());
//        }   else {
//            log.info("不启动");
//        }
//    }

    /**
     * Spring 初始化时执行，初始化 RPC 框架
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");

        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();

        // 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 启动服务器
        if (needServer) {
            HttpServer httpServer = new VertxHttpServer();
            httpServer.doStar(rpcConfig.getServerPort());
        } else {
            log.info("不启动 server");
        }

    }
}
                                                                                