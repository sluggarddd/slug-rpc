package com.slug.rpc.register;

/**
 * @author zhw
 * @version 0.1  15/10/27
 */
public interface RegisterConfig {

    /**
     * zookeeper客户端session超时时间
     */
    int ZK_SESSION_TIMEOUT = 5000;

    /**
     * zookeeper的znode根路径
     */
    String ZK_REGISTRY_PATH = "/registry";

    /**
     * zookeeper上注册的服务的根路径
     */
    String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/services";

}
