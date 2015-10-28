package com.slug.rpc.register;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

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
        if (zooKeeper != null) {
            watchNode(zooKeeper);
        }

    }

    private ZooKeeper connectServer() {

        ZooKeeper zooKeeper = null;

        try {
            zooKeeper = new ZooKeeper(registryAddress, RegisterConfig.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent watchedEvent) {

                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
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


    private void watchNode(final ZooKeeper zooKeeper) {

        try {
            List<String> nodeList = zooKeeper.getChildren(RegisterConfig.ZK_REGISTRY_PATH, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        watchNode(zooKeeper);
                    }
                }
            });

            List<String> dataList = new ArrayList<String>();
            for (String node : nodeList) {
                byte[] bytes = zooKeeper.getData(RegisterConfig.ZK_REGISTRY_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            this.dataList = dataList;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发现服务
     *
     * @return
     */
    public String discover() {
        String data = null;

        int size = dataList.size();

        //todo 可做负载均衡
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
            }

        }

        return data;

    }


}
