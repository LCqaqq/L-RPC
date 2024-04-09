package com.lc.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lc.example.common.model.User;
import com.lc.example.common.service.UserService;
import com.lc.lrpc.model.RpcRequest;
import com.lc.lrpc.model.RpcResponse;
import com.lc.lrpc.serializer.JdkSerializer;
import com.lc.lrpc.serializer.Serializer;

import java.io.IOException;

public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();
        //发请求
        RpcRequest rpcRequest =RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterType(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result = null;
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080").body(bodyBytes).execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
            return  (User) rpcResponse.getData();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
