package com.lc.lrpc.loadbalance;

import com.lc.lrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer{
    /**
     * 当前轮询下标
     */
    private final Random random = new Random();
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        //没有服务
        if (serviceMetaInfoList.isEmpty())
            return null;

        //只有一个服务，无需轮询
        if (serviceMetaInfoList.size() == 1)
            return serviceMetaInfoList.get(0);

        return serviceMetaInfoList.get(random.nextInt(serviceMetaInfoList.size()));

    }
}
