package com.lc.lrpc.loadbalance;

import com.lc.lrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    /**
     * 当前轮询下标
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()){
            //直接返回
            return null;
        }
        //只有一个服务，无需轮询
        if (serviceMetaInfoList.size() == 1)
            return serviceMetaInfoList.get(0);

        int index = currentIndex.getAndIncrement() % serviceMetaInfoList.size();
        return serviceMetaInfoList.get(index);


    }
}
