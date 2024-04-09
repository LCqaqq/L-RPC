package com.lc.example.consumer;

import com.lc.example.common.model.User;
import com.lc.example.common.service.UserService;
import com.lc.lrpc.proxy.ServiceProxyFactory;

/**
 * 简易消费者服务示例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
//        //静态代理
//        UserService userService = new UserServiceProxy();
        //动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
//        //todo 需要获取 UserService 的实现类对象
//        UserService userService = null;
        User user = new User();
        user.SetName("LC");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null){
            System.out.println(newUser.getName());
        }else {
            System.out.println("user == null");
        }
        long number  = userService.getNumber();
        System.out.println(number);
  }
}
