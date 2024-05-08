package com.lc.lrpc.example.consumer;

import com.lc.example.common.model.User;

import com.lc.example.common.service.UserService;
import com.lc.lrpc.springboot.starter.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService1;

    public void test(){
        User user = new User();
        user.SetName("LC");
        User resultUser = userService1.getUser(user);
        System.out.println(resultUser.getName());
    }
}
