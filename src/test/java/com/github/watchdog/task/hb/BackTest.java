package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.hubble.AbstractHubble;
import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.hubble.rule.RulesManager;
import com.github.hubble.series.CandleSeriesManager;
import com.github.watchdog.task.hb.hubble.BTC;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class BackTest {


    public static void main(String[] args) throws IOException {

        AbstractHubble btc = new BTC("Huobi", "btcusdt", "/tmp/state");
        btc.init();

        RulesManager rsm = btc.getRulesManager();

        CandleSeriesManager csm = btc.getCandleSeriesManager();
        for (CandleType candleType : Candles.candles) {
            if (CandleType.MIN_1 != candleType) {
                csm.candleSeriesBridge(CandleType.MIN_1, candleType);
            }
        }

        TreeMap<Long, CandleET> candleETTreeMap = Maps.newTreeMap();
        File file = new File("/tmp/r.log");
        List<String> lines = Files.readLines(file, Charset.defaultCharset());
        for (String line : lines) {
            if (StringUtils.isNotBlank(line)) {
                parse(line, candleETTreeMap);
            }
        }

        long splitTime = 1596384000;
        SortedMap<Long, CandleET> first = candleETTreeMap.headMap(splitTime);
        for (Map.Entry<Long, CandleET> entry : first.entrySet()) {
            btc.addCandleET(CandleType.MIN_1, entry.getValue(), false);
        }

        btc.getPositionManager().getStatus().set(true);

        SortedMap<Long, CandleET> second = candleETTreeMap.tailMap(splitTime);
        for (Map.Entry<Long, CandleET> entry : second.entrySet()) {
            CandleET cet = entry.getValue();
            List<CandleET> OLHC = Lists.newArrayList();
            OLHC.add(new CandleET(cet.getId(), cet.getOpen(), cet.getOpen(), cet.getOpen(), cet.getOpen(), cet.getAmount(), cet.getVolume(), cet.getCount()));
            if (cet.getClose() > cet.getOpen()) {
                OLHC.add(new CandleET(cet.getId(), cet.getOpen(), cet.getLow(), cet.getOpen(), cet.getLow(), cet.getAmount(), cet.getVolume(), cet.getCount()));
                OLHC.add(new CandleET(cet.getId(), cet.getOpen(), cet.getLow(), cet.getHigh(), cet.getHigh(), cet.getAmount(), cet.getVolume(), cet.getCount()));
            } else {
                OLHC.add(new CandleET(cet.getId(), cet.getOpen(), cet.getLow(), cet.getHigh(), cet.getHigh(), cet.getAmount(), cet.getVolume(), cet.getCount()));
                OLHC.add(new CandleET(cet.getId(), cet.getOpen(), cet.getLow(), cet.getOpen(), cet.getLow(), cet.getAmount(), cet.getVolume(), cet.getCount()));
            }
            OLHC.add(cet);
            for (CandleET candleET : OLHC) {
                btc.addCandleET(CandleType.MIN_1, candleET, false);
                for (CandleType candleType : Candles.candles) {
                    rsm.traverseRules(candleType, candleType.convert(candleET.getId()));
                }
            }
        }
    }


    private static void parse(String msg, TreeMap<Long, CandleET> candleETTreeMap) {

        PushMsg pushMsg = JSON.parseObject(msg, PushMsg.class);
        Validate.notNull(pushMsg.getRep());
        JSONArray array = JSON.parseArray(pushMsg.getData());
        for (int i = 0; i < array.size(); i++) {
            CandleET candleET = MarketConsumer.convert(array.getJSONObject(i));
            candleETTreeMap.put(candleET.getId(), candleET);
        }
    }

}
