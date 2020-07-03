package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.watchdog.dataobject.Candle;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.task.hb.dataobject.PushMsg;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MarketConsumer extends AbstractMarketConsumer {


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Huobi";
    }


    @Override protected void handle(String msg) {

        PushMsg pushMsg = JSON.parseObject(msg, PushMsg.class);

        //filter answers
        if (pushMsg.getCh() == null) {
            log.info("Recv : " + msg);
            return;
        }

        String[] items = pushMsg.getCh().split("\\.");
        String pairCodeName = items[1], type = items[2];

        switch (type) {
            case "kline":
                JSONObject data = JSON.parseObject(pushMsg.getTick());
                Candle currentCandle = Candle.builder().id(data.getLong("id"))
                        .low(data.getDouble("low")).high(data.getDouble("high")).open(data.getDouble("open"))
                        .close(data.getDouble("close")).vol(data.getDouble("amount")).build();
                handleCandle(pairCodeName, currentCandle);
                break;
        }
    }
}
