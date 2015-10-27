package com.slug.rpc.register;

import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhw
 * @version 0.1  15/10/27
 */
public class ServiceDiscovery {


    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<String>();

    private String registryAddress;

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;

        ZooKeeper zooKeeper = connectServer();
    }

    private ZooKeeper connectServer() {

        ZooKeeper zooKeeper = null;
        return null;
    }
}
