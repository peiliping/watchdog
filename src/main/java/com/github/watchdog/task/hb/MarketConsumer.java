package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hubble.ele.CandleET;
import com.github.watchdog.stream.AbstractMarketConsumer;
import com.github.watchdog.task.hb.dataobject.CandleType;
import com.github.watchdog.task.hb.dataobject.PushMsg;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;


public class MarketConsumer extends AbstractMarketConsumer {


    private Map<String, Symbol> symbols = Maps.newHashMap();


    public MarketConsumer(String config) {

        super(config);
        super.marketName = "Huobi";
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

        Symbol symbol = getOrcCreateSymbol(pairCodeName);
        if (reqOrsub) {
            List<CandleET> candleETList = Lists.newArrayList();
            JSONArray array = JSON.parseArray(pushMsg.getData());
            for (int i = 0; i < array.size(); i++) {
                CandleET candleET = convert(array.getJSONObject(i));
                candleETList.add(candleET);
            }
            symbol.initCandleETSeries(candleType, candleETList);
        } else {
            CandleET candleET = convert(JSON.parseObject(pushMsg.getTick()));
            symbol.addCandleET(candleType, candleET, true);
        }
    }


    private Symbol getOrcCreateSymbol(String name) {

        Symbol symbol = this.symbols.get(name);
        if (symbol == null) {
            symbol = new Symbol(super.marketName, name, getCandleShockRatioCondition(name));
            this.symbols.put(name, symbol);
        }
        return symbol;
    }


    private CandleET convert(JSONObject data) {

        CandleET candleET = new CandleET(data.getLong("id"),
                                         data.getDouble("open"), data.getDouble("low"), data.getDouble("high"), data.getDouble("close"),
                                         data.getDouble("amount"), data.getDouble("vol"), data.getInteger("count"));
        return candleET;
    }
}
