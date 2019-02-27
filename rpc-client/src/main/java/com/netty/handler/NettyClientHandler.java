package com.netty.handler;

import com.netty.server.NettyClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qll
 * @create 2019-02-27 20:12
 * @desc client handler
 **/
@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private NettyClient client;


}




























