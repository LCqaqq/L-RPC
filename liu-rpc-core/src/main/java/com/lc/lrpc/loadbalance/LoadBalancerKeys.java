package com.lc.lrpc.loadbalance;

/**
 * 负载均衡器常量
 */
public interface LoadBalancerKeys {

    String ROUND_ROBIN = "roundRobin";

    String RANDOM = "random";

    String CONSISTENT_HASH = "consistent_hash";

}
