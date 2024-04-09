package com.lc.lrpc.model;

import cn.hutool.core.util.StrUtil;
import com.lc.lrpc.constant.RpcConstant;
import lombok.Data;

/**
 * 服务源信息（注册信息）
 */
@Data
public class ServiceMetaInfo {
    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 版本号
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
    /**
     * 服务域名
     */
    private String serviceHost;
    /**
     * 服务端口号
     */
    private Integer servicePort;
    /**
     * 服务集群
     */
    private String serviceGroup = "default";

    /**
     * 获取服键名
     * @return
     */
    public String getServiceKey() {
        // 后续可扩展服务分组
//        return String.format("%s:%s:%s", serviceName, serviceVersion, serviceGroup);
        return String.format("%s:%s", serviceName, serviceVersion);
    }
    /**
     * 获取服务注册节点键名
     *
     * @return
     */
    public String getServiceNodeKey() {
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }
    /**
     * 获取服务注册节点键名
     * @return
     */
    public  String getServiceAddress() {
        if (!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
        }
        return String.format("%s:%s",serviceHost,servicePort);
    }

}
