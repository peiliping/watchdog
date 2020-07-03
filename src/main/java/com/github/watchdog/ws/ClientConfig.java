package com.github.watchdog.ws;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Builder
@Getter
@ToString
public class ClientConfig {


    private WebSocketSchema schema;

    private String host;

    private int port;

    private String path;

    private String heartBeatString;

    private int heartBeatInterval;

    private String subscribeString;

    private long subscribeInterval;
}
