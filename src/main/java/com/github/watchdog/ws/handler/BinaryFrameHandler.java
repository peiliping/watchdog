package com.github.watchdog.ws.handler;


import com.github.watchdog.common.Util;
import com.github.watchdog.stream.MsgChannel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) {

        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.content());
        String content = Util.unCompressGzip(byteBuf.array());
        if (log.isDebugEnabled()) {
            log.debug("data length : {} , content : {} ", content.length(), content);
        }
        MsgChannel.getInstance().addInput(content);
    }
}
