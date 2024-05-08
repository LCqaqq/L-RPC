package com.lc.lrpc.springboot.starter.bootstrap;

import com.lc.lrpc.RpcApplication;
import com.lc.lrpc.config.RegistryConfig;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.model.ServiceMetaInfo;
import com.lc.lrpc.registry.LocalRegistry;
import com.lc.lrpc.registry.Registry;
import com.lc.lrpc.registry.RegistryFactory;
import com.lc.lrpc.springboot.starter.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Bean初始化后执行注册服务
 */
@Slf4j
public class RpcProviderBootstrap implements BeanPostProcessor {
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        Class<?> beanClass = bean.getClass();
//        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
//        if (rpcService == null) {
//            Class<?> interfaceClass = rpcService.interfaceClass();
//            if (interfaceClass != void.class) {
//                interfaceClass = beanClass.getInterfaces()[0];
//            }
//            String serviceName = interfaceClass.getName();
//            String serviceVersion = rpcService.serviceVersion();
//
//            LocalRegistry.register(serviceName,beanClass);
//            //注册到服务中心
//            final RpcConfig rpcConfig =RpcApplication.getRpcConfig();
//            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
//            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
//            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
//            serviceMetaInfo.setServiceName(serviceName);
//            serviceMetaInfo.setServiceVersion(serviceVersion);
//            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
//            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
//            try {
//                registry.register(serviceMetaInfo);
//            } catch (Exception e) {
//                throw new RuntimeException(serviceName +"服务注册失败",e);
//            }
//        }
//        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
//    }
    /**
     * Bean 初始化后执行，注册服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            // 需要注册服务
            // 1. 获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            // 默认值处理
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            // 2. 注册服务
            // 本地注册
            LocalRegistry.register(serviceName, beanClass);

            // 全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
