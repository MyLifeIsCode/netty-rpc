package com.netty.coder;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author qll
 * @create 2019-02-27 16:54
 * @desc decoder
 **/
public class JSONDecoder extends LengthFieldBasedFrameDecoder {

    public JSONDecoder() {
        super(65535, 0, 4,0,4);
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode==null){
            return null;
        }
        int data_len = decode.readableBytes();
        byte[] bytes = new byte[data_len];
        decode.readBytes(bytes);
        Object parse = JSON.parse(bytes);
        return parse;
    }
}
