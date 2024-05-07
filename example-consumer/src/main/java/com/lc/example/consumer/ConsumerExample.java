package com.lc.example.consumer;

import com.lc.example.common.model.User;
import com.lc.example.common.service.UserService;
import com.lc.lrpc.bootstrap.ConsumerBootstrap;
import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.proxy.ServiceProxyFactory;
import com.lc.lrpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        ConsumerBootstrap.init();
//        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
//        System.out.println(rpc);
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.SetName("LL");
        User newUser = userService.getUser(user);
        if (newUser != null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null");
        }
    }
}
