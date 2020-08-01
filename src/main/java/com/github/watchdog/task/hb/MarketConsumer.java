package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.AbstractHubble;
import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.task.hb.dataobject.PushMsg;
import com.github.watchdog.task.hb.hubble.BTC;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


@Slf4j
public class MarketConsumer extends AbstractMarketConsumer {


    private Map<String, AbstractHubble> symbols = Maps.newHashMap();


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Huobi";

        AbstractHubble btc = new BTC(super.marketName, "btcusdt");
        this.symbols.put(btc.getName(), btc.init());
    }


    @Override protected void handle(String msg) {

        PushMsg pushMsg = JSON.parseObject(msg, PushMsg.class);

        boolean reqOrsub;
        String keyStr;
        if (pushMsg.getRep() != null) {
            keyStr = pushMsg.getRep();
            reqOrsub = true;
        } else if (pushMsg.getCh() != null) {
            keyStr = pushMsg.getCh();
            reqOrsub = false;
        } else {
            return;
        }

        String[] items = keyStr.split("\\.");
        String pairCodeName = items[1], type = items[2], step = items[3];

        if (!"kline".equals(type)) {
            return;
        }

        CandleType candleType = CandleType.of(step);
        if (candleType == null) {
            return;
        }

        AbstractHubble hubble = this.symbols.get(pairCodeName);
        if (hubble == null) {
            return;
        }

        if (reqOrsub) {
            List<CandleET> candleETList = Lists.newArrayList();
            JSONArray array = JSON.parseArray(pushMsg.getData());
            for (int i = 0; i < array.size(); i++) {
                CandleET candleET = convert(array.getJSONObject(i));
                candleETList.add(candleET);
            }
            log.info("Load history data from : " + candleETList.get(0).getId());
            hubble.addCandleETs(candleType, candleETList);
        } else {
            CandleET candleET = convert(JSON.parseObject(pushMsg.getTick()));
            hubble.addCandleET(candleType, candleET, true);
        }
    }


    private CandleET convert(JSONObject data) {

        return new CandleET(data.getLong("id"),
                            data.getDouble("open"), data.getDouble("low"), data.getDouble("high"), data.getDouble("close"),
                            data.getDouble("amount"), data.getDouble("vol"), data.getInteger("count"));
    }
}
