package com.netty.handle;

import com.alibaba.fastjson.JSON;
import com.netty.entity.Request;
import com.netty.entity.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author qll
 * @create 2019-02-27 13:20
 * @desc handel
 **/
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final Map<String,Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端连接成功!"+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接!{}",ctx.channel().remoteAddress());
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = JSON.parseObject(msg.toString(),Request.class);
        if("heartBeat".equals(request.getMethodName())){
            log.info("客户端心跳信息..."+ctx.channel().remoteAddress());
        }else {
            log.info("RPC客户端请求接口:"+request.getClassName()+"   方法名:"+request.getMethodName());
            Response response = new Response();
            response.setRequestId(request.getId());
            try {
                Object result = this.handler(request);
                response.setData(result);
            } catch (Throwable e) {
                e.printStackTrace();
                response.setCode(1);
                response.setError_msg(e.toString());
                log.error("RPC Server handle request error",e);
            }
            ctx.writeAndFlush(response);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    /**
     * 通过反射，指定本地方法
     * @param request
     * @return
     * @throws Throwable
     */
    private Object handler(Request request) throws Throwable{
        String className = request.getClassName();
        Object serviceBean = serviceMap.get(className);
        if(serviceBean != null){
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] paramterTypes = request.getParameterTypes();
            Object[] paramters = request.getParameters();

            Method method = serviceClass.getMethod(methodName,paramterTypes);
            //明显 Accessible并不是标识方法能否访问的. public的方法 Accessible仍为false
            //使用了method.setAccessible(true)后 性能有了20倍的提升
            //Accessable属性是继承自AccessibleObject 类. 功能是启用或禁用安全检查
            //使private方法可以被调用
            method.setAccessible(true);
            return method.invoke(paramters);
        }else {
            throw new Exception("未找到服务接口,请检查配置!:"+className+"#"+request.getMethodName());
        }
    }

    /**
     * 获取参数列表
     * @param paramterTypes
     * @param paramters
     * @return
     */
    public Object[] getParamters(Class<?>[] paramterTypes,Object[] paramters){
        if(paramters == null || paramters.length == 0){
            return paramters;
        }else {
            Object[] new_paramters = new Object[paramters.length];
            for (int i=0;i<paramters.length;i++){
                new_paramters[i] = JSON.parseObject(paramters[i].toString(),paramterTypes[i]);
            }
            return new_paramters;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if(event.state() == IdleState.ALL_IDLE){
                log.info("客户端已超过60秒未读写数据，关闭连接.{}",ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        ctx.close();
//        super.exceptionCaught(ctx, cause);
    }
}
































