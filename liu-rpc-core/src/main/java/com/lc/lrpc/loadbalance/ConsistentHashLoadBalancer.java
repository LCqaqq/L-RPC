package com.lc.lrpc.loadbalance;

import com.lc.lrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHashLoadBalancer implements LoadBalancer {

    private final TreeMap<Integer,ServiceMetaInfo> virtualNodes = new TreeMap<>();

    private static  final  int VIRTUAL_NODE_NUM = 100;

    /**
     * 先构造若干个虚拟节点
     * 求出请求的hash值，匹配给最近的前一个节点
     * @param requestParams
     * @param serviceMetaInfoList
     * @return
     */
    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {

       if (serviceMetaInfoList.isEmpty())
           return null;

       for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList){
           for (int i=0 ; i<VIRTUAL_NODE_NUM;i++){
               int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" +i);
               virtualNodes.put(hash,serviceMetaInfo);
           }
       }

       int hash = getHash(requestParams);

       Map.Entry<Integer,ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
       if (entry == null){
           entry = virtualNodes.firstEntry();
       }

           return entry.getValue();
    }

    private int getHash(Object key) {
        return key.hashCode();
    }
}
