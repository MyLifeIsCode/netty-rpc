package com.netty.registry;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author qll
 * @create 2019-02-27 14:01
 * @desc 服务注册
 **/
@Component
@Slf4j
public class ServiceRegistry {

    @Value("${registry.address}")
    private String registryAddress;

    private static final String ZK_REGISTRY_PATH = "/rpc";

    public void register(String data){
        if(data != null){
            ZkClient zkClient = connectServer();
            addRootNode(zkClient);
            createNode(zkClient,data);
        }
    }

    /**
     * 连接zk
     * @return ZkClient
     */
    private ZkClient connectServer(){
        ZkClient client = new ZkClient(registryAddress,20000,20000);
        return client;
    }

    /**
     * 创建根目录 /rpc
     * @param zkClient
     */
    private void addRootNode(ZkClient zkClient){
        boolean exists = zkClient.exists(ZK_REGISTRY_PATH);
        if(!exists){
            zkClient.createPersistent(ZK_REGISTRY_PATH);
            log.info("创建zookeeper主节点 {}",ZK_REGISTRY_PATH);
        }
    }

    private void createNode(ZkClient zkClient,String data){
        String path = zkClient.create(ZK_REGISTRY_PATH + "/provider",data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("创建zookeeper数据节点 ({} => {})", path, data);
    }

}























