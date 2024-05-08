package com.lc.lrpc.example.provider;

import com.lc.example.common.model.User;
import com.lc.example.common.service.UserService;
import com.lc.lrpc.springboot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@RpcService
@Service
public class UserServiceImpl implements UserService {
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }

}
