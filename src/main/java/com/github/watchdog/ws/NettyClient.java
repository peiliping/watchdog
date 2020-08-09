package com.github.watchdog.ws;


import com.github.watchdog.common.Util;
import com.github.watchdog.stream.MsgChannel;
import com.github.watchdog.ws.handler.BinaryFrameHandler;
import com.github.watchdog.ws.handler.TextFrameHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
public class NettyClient {


    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

    private final ClientConfig config;

    private final String url;

    private final String[] subscribeItems;

    private Bootstrap bootstrap;

    private EventLoopGroup eventLoopGroup;

    private Channel channel;


    public NettyClient(ClientConfig config) {

        this.config = config;
        this.url = String.format("%s://%s:%s%s", this.config.getSchema().schema, this.config.getHost(), this.config.getPort(), this.config.getPath());
        this.subscribeItems = config.getSubscribeString().split("\\$");
        this.executor.scheduleWithFixedDelay(() -> heartBeat(), 5, config.getHeartBeatInterval(), TimeUnit.SECONDS);
    }


    public void connect() {

        this.bootstrap = new Bootstrap();
        if (this.eventLoopGroup != null) {
            this.eventLoopGroup.shutdownGracefully();
        }
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap.group(this.eventLoopGroup);
        this.bootstrap.channel(NioSocketChannel.class);

        this.bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {


            protected void initChannel(SocketChannel socketChannel) throws Exception {

                ChannelPipeline cp = socketChannel.pipeline();

                if (WebSocketSchema.WSS == config.getSchema()) {
                    SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                    cp.addLast("ssl", sslCtx.newHandler(socketChannel.alloc(), config.getHost(), config.getPort()));
                }

                cp.addLast("netty-log", new LoggingHandler("NettyLog", LogLevel.TRACE));
                cp.addLast("http-codec", new HttpClientCodec());
                cp.addLast("http-chunked", new ChunkedWriteHandler());
                cp.addLast("http-aggregator", new HttpObjectAggregator(64 * 1024));
                cp.addLast("ws-aggregator", new WebSocketFrameAggregator(64 * 1024));

                WebSocketClientProtocolConfig protocolConfig = WebSocketClientProtocolConfig.newBuilder()
                        .webSocketUri(URI.create(url))
                        .subprotocol(null)
                        .version(WebSocketVersion.V13)
                        .allowExtensions(true)
                        .customHeaders(EmptyHttpHeaders.INSTANCE)
                        .maxFramePayloadLength(64 * 1024)
                        .performMasking(true)
                        .allowMaskMismatch(false)
                        .handleCloseFrames(true)
                        .sendCloseFrame(WebSocketCloseStatus.NORMAL_CLOSURE)
                        .dropPongFrames(false)
                        .handshakeTimeoutMillis(10 * 1000L)
                        .forceCloseTimeoutMillis(-1)
                        .absoluteUpgradeUrl(false)
                        .build();
                cp.addLast("ws-protocol", new WebSocketClientProtocolHandler(protocolConfig));
                cp.addLast("ws-text", new TextFrameHandler());
                cp.addLast("ws-binary", new BinaryFrameHandler());
                cp.addLast("retry", new ChannelInboundHandlerAdapter() {


                    @Override public void channelInactive(ChannelHandlerContext ctx) throws Exception {

                        log.error("netty connect inactive .");
                        executor.schedule(() -> connect(), 3, TimeUnit.SECONDS);
                        super.channelInactive(ctx);
                    }
                });
            }
        });
        ChannelFuture channelFuture = this.bootstrap.connect(this.config.getHost(), this.config.getPort());
        this.channel = channelFuture.channel();
        channelFuture.addListener((ChannelFutureListener) future -> {

            if (future.isSuccess()) {
                log.info("netty connect success .");
                executor.schedule(() -> subscribe(), 3, TimeUnit.SECONDS);
            } else {
                log.error("netty connect error : ", future.cause());
                executor.schedule(() -> connect(), 3, TimeUnit.SECONDS);
            }
        });
    }


    private void subscribe() {

        Channel tmpChannel = this.channel;
        if (tmpChannel != null && tmpChannel.isActive()) {
            MsgChannel.getInstance().addInput(MsgChannel.CMD_RESTART);
            for (String item : this.subscribeItems) {
                log.info("subscribe : " + item);
                TextWebSocketFrame frame = new TextWebSocketFrame(item);
                tmpChannel.writeAndFlush(frame);
                Util.sleepMS(this.config.getSubscribeInterval());
            }
        }
    }


    private void heartBeat() {

        Channel tmpChannel = this.channel;
        if (tmpChannel != null && tmpChannel.isActive()) {
            long now = Util.nowMS();
            String hbStr = String.format(this.config.getHeartBeatString(), now);
            tmpChannel.writeAndFlush(new TextWebSocketFrame(hbStr));
        }
    }
}
