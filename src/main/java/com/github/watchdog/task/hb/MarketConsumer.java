package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.Series;
import com.github.hubble.ele.CandleET;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.task.hb.dataobject.PushMsg;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class MarketConsumer extends AbstractMarketConsumer {


    private Map<String, Series<CandleET>> candles = Maps.newHashMap();


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Huobi";
    }


    @Override protected void handle(String msg) {

        PushMsg pushMsg = JSON.parseObject(msg, PushMsg.class);

        String pairCodeName = null, type = null;

        if (pushMsg.getRep() != null) {
            String[] items = pushMsg.getRep().split("\\.");
            pairCodeName = items[1];
            type = items[2];

            JSONArray data = JSON.parseArray(pushMsg.getData());

        }

        if (pushMsg.getCh() != null) {
            String[] items = pushMsg.getCh().split("\\.");
            pairCodeName = items[1];
            type = items[2];

            if ("kline".equals(type)) {
                JSONObject data = JSON.parseObject(pushMsg.getTick());
                CandleET currentCandle = new CandleET(data.getLong("id"));
                currentCandle.setLow(data.getDouble("low"));
                currentCandle.setHigh(data.getDouble("high"));
                currentCandle.setOpen(data.getDouble("open"));
                currentCandle.setClose(data.getDouble("close"));
                currentCandle.setAmount(data.getDouble("amount"));
                currentCandle.setVolume(data.getDouble("vol"));
                currentCandle.setCount(data.getInteger("count"));

            }
        }
    }
}
