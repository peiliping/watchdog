package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.Series;
import com.github.hubble.Symbol;
import com.github.hubble.ele.CandleET;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.task.hb.dataobject.CandleType;
import com.github.watchdog.task.hb.dataobject.PushMsg;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
public class MarketConsumer extends AbstractMarketConsumer {


    private Map<String, Symbol> symbols = Maps.newHashMap();


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Huobi";
    }


    @Override protected void handle(String msg) {

        PushMsg pushMsg = JSON.parseObject(msg, PushMsg.class);

        boolean reqOrsub = false;
        String keyStr = null;
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

        pairCodeName = super.marketName + "." + pairCodeName;

        Symbol symbol = getOrcCreateSymbol(pairCodeName, candleType);
        if (reqOrsub) {
            JSONArray array = JSON.parseArray(pushMsg.getData());
            for (int i = 0; i < array.size(); i++) {
                CandleET candleET = convert(array.getJSONObject(i));
                addCandle(candleET, symbol);
            }
            symbol.initRule();
        } else {
            CandleET candleET = convert(JSON.parseObject(pushMsg.getTick()));
            addCandle(candleET, symbol);
            symbol.getRulesManager().traverseRules(candleET.getId());
        }
    }


    private Symbol getOrcCreateSymbol(String name, CandleType candleType) {

        Symbol symbol = this.symbols.get(name);
        if (symbol == null) {
            symbol = new Symbol(name, candleType.interval);
            symbol.initIndicators();
            this.symbols.put(name, symbol);
        }
        return symbol;
    }


    private CandleET convert(JSONObject data) {

        CandleET candleET = new CandleET(data.getLong("id"));
        candleET.setLow(data.getDouble("low"));
        candleET.setHigh(data.getDouble("high"));
        candleET.setOpen(data.getDouble("open"));
        candleET.setClose(data.getDouble("close"));
        candleET.setAmount(data.getDouble("amount"));
        candleET.setVolume(data.getDouble("vol"));
        candleET.setCount(data.getInteger("count"));
        return candleET;
    }


    private void addCandle(CandleET candleET, Symbol symbol) {

        Series<CandleET> candleETSeries = symbol.getCandleETSeries();
        if (candleET.getId() >= candleETSeries.getMaxId()) {
            candleETSeries.add(candleET);
        }
    }
}
