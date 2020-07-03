package com.github.watchdog.ws.handler;


import com.github.watchdog.stream.MsgChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {

        String text = msg.text();
        if (log.isDebugEnabled()) {
            log.debug("data length : {} , content : {} ", text.length(), text);
        }
        MsgChannel.getInstance().addInput(text);
    }
}
