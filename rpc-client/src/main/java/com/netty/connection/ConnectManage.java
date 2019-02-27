package com.netty.connection;

import com.netty.server.NettyClient;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qll
 * @create 2019-02-27 20:50
 * @desc connection
 **/
@Slf4j
public class ConnectManage {

    @Resource
    private NettyClient nettyClient;

    private AtomicInteger roundRobin = new AtomicInteger(0);
    private CopyOnWriteArrayList<Channel> channels = new CopyOnWriteArrayList<>();
    private Map<SocketAddress,Channel> channelNodes = new ConcurrentHashMap<>();

    public Channel chooseChannel(){
        if(channels.size() > 0){
            int size = channels.size();
            int index = (roundRobin.getAndAdd(1) + size) % size;
            return channels.get(index);
        }else {
            return null;
        }
    }

    public synchronized void updateConnectServer(List<String> addressList){
        if(addressList == null || addressList.size() == 0){
            log.error("没有可用的服务器节点, 全部服务节点已关闭!");
            for (Channel channel : channels){
                SocketAddress remotePeer = channel.remoteAddress();
                Channel handler_node = channelNodes.get(remotePeer);
                handler_node.close();
            }
            channels.clear();
            channelNodes.clear();
            return;
        }
        HashSet<SocketAddress> newAllServerNodeSet = new HashSet<>();
        for(int i = 0;i < addressList.size();i++){
            String[] array = addressList.get(i).split(":");

        }
    }
}














































