package com.github.watchdog.task.hb;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.hubble.AbstractHubble;
import com.github.hubble.common.CandleType;
import com.github.hubble.ele.CandleET;
import com.github.hubble.series.CandleSeriesManager;
import com.github.hubble.signal.Signal;
import com.github.watchdog.task.hb.hubble.BTC;
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

        //清理历史状态
        String statePath = "/tmp/state";
        File stateFile = new File(statePath);
        stateFile.delete();

        //初始化
        AbstractHubble btc = new BTC("Huobi", "btcusdt", statePath).init();

        //k线建立桥接关系
        CandleSeriesManager csm = btc.getCandleSeriesManager();
        for (CandleType candleType : Candles.candles) {
            if (CandleType.MIN_1 != candleType) {
                csm.candleSeriesBridge(CandleType.MIN_1, candleType);
            }
        }

        //加载历史K线数据
        String dataFilePath = "/tmp/history.log";
        TreeMap<Long, CandleET> candleETTreeMap = Maps.newTreeMap();
        File file = new File(dataFilePath);
        List<String> lines = Files.readLines(file, Charset.defaultCharset());
        for (String line : lines) {
            if (StringUtils.isNotBlank(line)) {
                parse(line, candleETTreeMap);
            }
        }

        //先加载一些数据预热
        long splitTime = 1593619200;
        SortedMap<Long, CandleET> first = candleETTreeMap.headMap(splitTime);
        for (Map.Entry<Long, CandleET> entry : first.entrySet()) {
            btc.addCandleET(CandleType.MIN_1, entry.getValue(), false);
        }

        //启动仓位管理
        btc.getPositionManager().getStatus().set(true);

        //模拟k线数据增量获取
        SortedMap<Long, CandleET> second = candleETTreeMap.tailMap(splitTime);
        for (Map.Entry<Long, CandleET> entry : second.entrySet()) {
            List<CandleET> OLHC = entry.getValue().split4BackTest();
            for (CandleET candleET : OLHC) {
                btc.addCandleET(CandleType.MIN_1, candleET, false);
                for (CandleType candleType : Candles.candles) {
                    btc.getRulesManager().traverseRules(candleType, candleType.convert(candleET.getId()));
                }
            }
        }

        btc.getPositionManager().handleSignal(Signal.MUCK, btc.getCandleSeriesManager().getLastCandleET(CandleType.MIN_1).getClose());
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
