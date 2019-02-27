package com.netty.server;

import com.netty.annotation.RpcService;
import com.netty.coder.JSONDecoder;
import com.netty.coder.JSONEncoder;
import com.netty.handle.NettyServerHandler;
import com.netty.handle.RpcServerInitializer;
import com.netty.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.Resource;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qll
 * @create 2019-02-27 13:56
 * @desc server
 * 为了方便管理，我们把它也注册成Bean，同时实现ApplicationContextAware接口，
 * 把上面@RpcService注解的服务类捞出来，缓存起来，供消费者调用。
 * 同时，作为服务器，还要对客户端的链路进行心跳检测，超过60秒未读写数据，关闭此连接。
 **/
@Slf4j
@Component
public class NettyServer implements ApplicationContextAware,InitializingBean {


    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(3);
    private Map<String,Object> serviceMap = new HashMap<>();

    @Value("${rpc.server.address}")
    private String serverAddress;

    @Resource
    private ServiceRegistry registry;

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取所有RpcService
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object serviceBean : beans.values()){
            Class<?> clazz = serviceBean.getClass();
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> inter : interfaces){
                String interfaceName = inter.getName();
                log.info("加载服务类: {}",interfaceName);
                serviceMap.put(interfaceName,serviceBean);
            }
        }
        log.info("已经加载全部服务接口:{}",serviceMap);
    }

    public void start(){
        final NettyServerHandler handler = new NettyServerHandler(serviceMap);
        new Thread(()->{
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                EventLoopGroup group = new NioEventLoopGroup();
                bootstrap
                        .group(bossGroup,workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG,1024)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                ChannelPipeline pipeline = socketChannel.pipeline();
                                pipeline.addLast(new IdleStateHandler(0,0,60));
                                pipeline.addLast(new JSONEncoder());
                                pipeline.addLast(new JSONDecoder());
                                pipeline.addLast(handler);
                            }
                        });
                String[] array = serverAddress.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                ChannelFuture sync = bootstrap.bind(host, port).sync();
                log.info("RPC 服务器启动，监听端口:{}",port);
                registry.register(serverAddress);
            }catch (Exception e){
                e.printStackTrace();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();
    }
}











































