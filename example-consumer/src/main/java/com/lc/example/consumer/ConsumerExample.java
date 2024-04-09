package com.lc.example.consumer;

import com.lc.lrpc.config.RpcConfig;
import com.lc.lrpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class,"rpc");
        System.out.println(rpc);
    }
}
