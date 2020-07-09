package com.github.watchdog.task.kb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.hubble.ele.CandleET;
import com.github.watchdog.common.Util;
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
                CandleET currentCandle = new CandleET(data.get(0).longValue());
                currentCandle.setLow(data.get(1));
                currentCandle.setHigh(data.get(2));
                currentCandle.setOpen(data.get(3));
                currentCandle.setClose(data.get(4));
                currentCandle.setAmount(data.get(5));
                handleCandle(pushMsg.getPairCode(), currentCandle);
                break;
        }
    }
}
