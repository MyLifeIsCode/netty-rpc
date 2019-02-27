package com.netty.handle;

import com.netty.coder.JSONDecoder;
import com.netty.coder.JSONEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author qll
 * @create 2019-02-27 16:32
 * @desc initializer
 **/
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

    }
}
