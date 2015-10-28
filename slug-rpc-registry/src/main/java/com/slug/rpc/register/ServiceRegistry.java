package com.slug.rpc.register;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhw
 * @version 0.1  15/10/28
 */
public class ServiceRegistry {


    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    /**
     * 服务注册类
     *
     * @param registryAddress
     */
    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }


    public void register(String serviceName) {
        if (serviceName != null) {

            ZooKeeper zooKeeper = connectServer();

            if (zooKeeper != null) {
                createdNode(zooKeeper, serviceName);
            }
        }

    }

    private ZooKeeper connectServer() {
        ZooKeeper zooKeeper = null;

        try {
            zooKeeper = new ZooKeeper(registryAddress, RegisterConfig.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        // 发送时间让阻塞线程继续执行
                        latch.countDown();
                    }
                }
            });

            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return zooKeeper;
    }

    private void createdNode(ZooKeeper zooKeeper, String data) {

        try {
            byte[] bytes = data.getBytes();
            String path = zooKeeper.create(RegisterConfig.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(path);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
