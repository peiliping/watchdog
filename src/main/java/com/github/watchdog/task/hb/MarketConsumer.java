package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.stream.MsgChannel;
import com.github.watchdog.task.hb.hubble.BTC;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


@Slf4j
public class MarketConsumer extends AbstractMarketConsumer {


    private Map<CandleType, List<CandleET>> buffer = Maps.newHashMap();

    private Map<CandleType, Boolean> getHistoryData = Maps.newHashMap();

    private boolean increment = false;


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Huobi";
        super.hubble = new BTC(super.marketName, "btcusdt", "/root/watchdog2/state");
        super.hubble.init();
        initCandleType();
    }


    private void initCandleType() {

        for (CandleType candleType : Candles.candles) {
            this.getHistoryData.put(candleType, false);
        }
    }


    private void checkOpenIncrement() {

        for (Map.Entry<CandleType, Boolean> entry : this.getHistoryData.entrySet()) {
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
        super.hubble.getPositionManager().getStatus().set(true);
    }


    @Override protected void handle(String msg) {

        if (MsgChannel.CMD_RESTART.equals(msg)) {
            initCandleType();
            this.increment = false;
            super.hubble.getPositionManager().getStatus().set(false);
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
        if (candleType == null || !this.getHistoryData.containsKey(candleType)) {
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
            this.getHistoryData.put(candleType, true);
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
