package com.netty.server;

import com.alibaba.fastjson.JSONArray;
import com.netty.codec.json.JSONDecoder;
import com.netty.codec.json.JSONEncoder;
import com.netty.connection.ConnectManage;
import com.netty.entity.Request;
import com.netty.entity.Response;
import com.netty.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.SocketAddress;
import java.util.concurrent.SynchronousQueue;

/**
 * @author qll
 * @create 2019-02-27 20:11
 * @desc client
 **/
@Component
@Slf4j
public class NettyClient {

    private EventLoopGroup group = new NioEventLoopGroup(1);
    private Bootstrap bootstrap = new Bootstrap();

    @Resource
    private NettyClientHandler clientHandler;

    @Resource
    private ConnectManage connectManage;

    public NettyClient(){
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0,0,30));
                        pipeline.addLast(new JSONEncoder());
                        pipeline.addLast(new JSONDecoder());
                        pipeline.addLast("handler",clientHandler);
                    }
                });
    }

    @PreDestroy
    public void destroy(){
        log.info("RPC客户端退出，释放资源");
        group.shutdownGracefully();
    }

    public Object send(Request request) throws InterruptedException{
        Channel channel = connectManage.chooseChannel();
        if(channel != null && channel.isActive()){
            SynchronousQueue<Object> queue = clientHandler.sendRequest(request,channel);
            Object result = queue.take();
            return JSONArray.toJSONString(request);
        }else {
            Response response = new Response();
            response.setCode(1);
            response.setError_msg("未正确连接到服务器.请检查相关配置信息!");
            return JSONArray.toJSONString(response);
        }
    }

    public Channel doConnect(SocketAddress address) throws InterruptedException{
        ChannelFuture future = bootstrap.connect(address);
        Channel channel = future.sync().channel();
        return channel;
    }
}




























