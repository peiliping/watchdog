package com.github.watchdog.task.kb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.watchdog.common.Util;
import com.github.watchdog.dataobject.Candle;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.task.kb.dataobject.PushMsg;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.List;


@Slf4j
public class MarketConsumer extends AbstractMarketConsumer {


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Kangbo";
    }


    @Override protected void handle(String msg) {

        PushMsg pushMsg = JSON.parseObject(msg, PushMsg.class);

        //filter answers
        if (pushMsg.getEvent() != null) {
            log.info("Recv : " + msg);
            return;
        }

        String dataStr = pushMsg.getZip() ? Util.unCompressGzip(Base64.getDecoder().decode(pushMsg.getData())) : pushMsg.getData();

        switch (pushMsg.getType()) {
            case "candles":
                List<Double> data = JSON.parseObject(dataStr, new TypeReference<List<List<Double>>>() {


                }).get(0);
                Candle currentCandle = Candle.builder().id(data.get(0).longValue())
                        .low(data.get(1)).high(data.get(2)).open(data.get(3)).close(data.get(4)).vol(data.get(5)).build();
                handleCandle(pushMsg.getPairCode(), currentCandle);
                break;
        }
    }
}
