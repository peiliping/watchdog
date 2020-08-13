package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.stream.MsgChannel;
import com.github.watchdog.task.hb.hubble.BTC_UP;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


@Slf4j
public class MarketConsumer extends AbstractMarketConsumer {


    private final Map<CandleType, List<CandleET>> buffer = Maps.newHashMap();

    private final Map<CandleType, Boolean> hasHistoryData = Maps.newHashMap();

    private boolean increment = false;


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Huobi";
        super.hubble = new BTC_UP(super.marketName, "btcusdt", "/root/watchdog2/state");
        super.hubble.init();
        initHistoryDataMap();
    }


    private void initHistoryDataMap() {

        for (CandleType candleType : Candles.candles) {
            this.hasHistoryData.put(candleType, false);
        }
    }


    private void checkOpenIncrement() {

        for (Map.Entry<CandleType, Boolean> entry : this.hasHistoryData.entrySet()) {
            if (!entry.getValue()) {
                return;
            }
        }
        if (!this.buffer.isEmpty()) {
            for (Map.Entry<CandleType, List<CandleET>> entry : this.buffer.entrySet()) {
                super.hubble.addCandleETs(entry.getKey(), entry.getValue());
            }
            this.buffer.clear();
        }
        this.increment = true;
        super.hubble.getPositionManager().open();
    }


    @Override protected void handle(String msg) {

        if (MsgChannel.CMD_RESTART.equals(msg)) {
            initHistoryDataMap();
            this.increment = false;
            super.hubble.getPositionManager().close();
            return;
        }

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

        if (!super.hubble.getName().equals(pairCodeName)) {
            return;
        }
        if (!"kline".equals(type)) {
            return;
        }
        CandleType candleType = CandleType.of(step);
        if (candleType == null || !this.hasHistoryData.containsKey(candleType)) {
            return;
        }

        if (reqOrsub) {
            List<CandleET> candleETList = Lists.newArrayList();
            JSONArray array = JSON.parseArray(pushMsg.getData());
            for (int i = 0; i < array.size(); i++) {
                CandleET candleET = convert(array.getJSONObject(i));
                candleETList.add(candleET);
            }
            super.hubble.addCandleETs(candleType, candleETList);
            this.hasHistoryData.put(candleType, true);
            checkOpenIncrement();
        } else {
            CandleET candleET = convert(JSON.parseObject(pushMsg.getTick()));
            if (this.increment) {
                super.hubble.addCandleET(candleType, candleET, true);
            } else {
                getQFromBuffer(candleType).add(candleET);
            }
        }
    }


    private List<CandleET> getQFromBuffer(CandleType candleType) {

        List<CandleET> queue = this.buffer.get(candleType);
        if (queue == null) {
            queue = Lists.newArrayList();
            this.buffer.put(candleType, queue);
        }
        return queue;
    }


    public static CandleET convert(JSONObject data) {

        return new CandleET(data.getLong("id"),
                            data.getDouble("open"), data.getDouble("low"), data.getDouble("high"), data.getDouble("close"),
                            data.getDouble("amount"), data.getDouble("vol"), data.getInteger("count"));
    }
}
