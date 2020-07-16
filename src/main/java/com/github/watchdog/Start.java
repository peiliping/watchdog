package com.github.watchdog;


import com.github.watchdog.common.Util;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.stream.BarkConsumer;
import com.github.watchdog.stream.MsgChannel;
import com.github.watchdog.ws.ClientConfig;
import com.github.watchdog.ws.NettyClient;
import com.github.watchdog.ws.WebSocketSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;

import static com.github.watchdog.CMD.*;


@Slf4j
public class Start {


    public static void main(String[] args) {

        try {
            CommandLine commandLine = (new DefaultParser()).parse(CMD.OPTIONS, args);
            if (CMD.HELP(commandLine)) {
                return;
            }

            Util.updateLogLevel(getValue(commandLine, LOGLEVEL, s -> s, "INFO"));

            final ClientConfig clientConfig = ClientConfig.builder()
                    .schema(hasOption(commandLine, SSL) ? WebSocketSchema.WSS : WebSocketSchema.WS)
                    .host(getValueForce(commandLine, HOST, s -> s))
                    .port(getValue(commandLine, PORT, Integer::parseInt, 443))
                    .path(getValue(commandLine, PATH, s -> s, "/ws"))
                    .heartBeatString(getValueForce(commandLine, HEARTBEAT, s -> s))
                    .heartBeatInterval(getValue(commandLine, HEARTBEATINTERVAL, Integer::parseInt, 10))
                    .subscribeString(getValueForce(commandLine, SUBSCRIBE, s -> s))
                    .subscribeInterval(getValue(commandLine, SUBSCRIBEINTERVAL, Long::parseLong, 10L))
                    .build();
            log.info(clientConfig.toString());
            new NettyClient(clientConfig).connect();

            final String market = getValueForce(commandLine, MARKET, s -> s);
            final String marketConfig = getValue(commandLine, MARKETCONFIG, s -> s, null);
            String mcClassName = "com.github.watchdog.task." + market.trim().toLowerCase() + ".MarketConsumer";
            Class<? extends AbstractMarketConsumer> mcClass = (Class<? extends AbstractMarketConsumer>) Class.forName(mcClassName);
            AbstractMarketConsumer marketConsumer = mcClass.getConstructor(String.class).newInstance(marketConfig);
            namedThreadStart(marketConsumer);

            String[] uids = null;
            if (hasOption(commandLine, BARKIDS)) {
                final String barkIds = getValueForce(commandLine, BARKIDS, s -> s);
                uids = barkIds.split(",");
            }
            namedThreadStart(new BarkConsumer(uids, hasOption(commandLine, BARKIDS)));

            while (true) {
                Util.sleepSec(60);
                long delta = Util.nowMS() - MsgChannel.getInstance().getLastMsgTime();
                if (delta > 5 * 60 * 1000) {
                    if (uids != null) {
                        Util.sendMsg(uids[0], "watchdog-nodata-error");
                    }
                    System.exit(0);
                }
            }
        } catch (Exception e) {
            log.error("start error : ", e);
            System.exit(0);
        }
    }


    private static void namedThreadStart(Runnable runnable) {

        String name = "WatchDog-" + runnable.getClass().getSimpleName();
        new Thread(runnable, name).start();
    }
}
