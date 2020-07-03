package com.github.watchdog.stream;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.watchdog.common.Util;
import com.github.watchdog.dataobject.Candle;
import com.github.watchdog.dataobject.LastNQueue;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.Map;


@Slf4j
public abstract class AbstractMarketConsumer extends AbstractConsumer {


    protected Map<String, Long> lastNotifyFilter = Maps.newHashMap();

    protected long notifyInterval = 10 * 60L;

    protected Map<String, LastNQueue<Candle>> lastCandles = Maps.newHashMap();

    protected int candleQueueSize = 6;

    protected Map<String, Double> candleChangeRatioConditions = Maps.newHashMap();

    protected double defaultCandleChangeRatioCondition = 2D;


    protected String marketName = "Unknown";


    public AbstractMarketConsumer(String config) {

        if (StringUtils.isNotBlank(config)) {
            log.info("marketConsumer config : " + config);

            JSONObject jsonObject = JSON.parseObject(config);
            Long tNotifyInterval = jsonObject.getLong("notifyInterval");
            if (tNotifyInterval != null) {
                this.notifyInterval = tNotifyInterval;
            }
            Integer tCandleQueueSize = jsonObject.getInteger("candleQueueSize");
            if (tCandleQueueSize != null) {
                this.candleQueueSize = tCandleQueueSize;
            }
            Double tDefaultCandleChangeRatioCondition = jsonObject.getDouble("defaultCandleChangeRatioCondition");
            if (tDefaultCandleChangeRatioCondition != null) {
                this.defaultCandleChangeRatioCondition = tDefaultCandleChangeRatioCondition;
            }
            JSONObject tJCandleChangeRatioConditions = jsonObject.getJSONObject("candleChangeRatioConditions");
            if (tJCandleChangeRatioConditions != null) {
                for (String key : tJCandleChangeRatioConditions.keySet()) {
                    this.candleChangeRatioConditions.put(key, tJCandleChangeRatioConditions.getDouble(key));
                }
            }
        }
    }


    protected void output(String key, String msg) {

        long lastNotifyTime = this.lastNotifyFilter.getOrDefault(key, 0L);
        long now = Util.nowSec();
        if (now - lastNotifyTime >= this.notifyInterval) {
            super.msgChannel.addResult(msg);
            this.lastNotifyFilter.put(key, now);
        }
    }


    protected void handleCandle(String pairCodeName, Candle currentCandle) {

        LastNQueue<Candle> oldLNQ = this.lastCandles.get(pairCodeName);
        if (oldLNQ == null) {
            oldLNQ = new LastNQueue<>(this.candleQueueSize);
            this.lastCandles.put(pairCodeName, oldLNQ);
        }
        oldLNQ.add(currentCandle);

        Candle lowCandle = oldLNQ.getList().stream().min(Comparator.comparingDouble(Candle::getLow)).get();
        Candle highCandle = oldLNQ.getList().stream().max(Comparator.comparingDouble(Candle::getHigh)).get();
        if (currentCandle.getId() != lowCandle.getId() && currentCandle.getId() != highCandle.getId()) {
            return;
        }

        Double delta = highCandle.getHigh() - lowCandle.getLow();
        Double ratio = (double) Math.round(delta / lowCandle.getLow() * 10000) / 100;
        double condition = this.candleChangeRatioConditions.getOrDefault(pairCodeName, this.defaultCandleChangeRatioCondition);
        if (ratio < condition) {
            return;
        }

        String direction = "~";
        if (lowCandle.getId() < highCandle.getId()) {
            direction = "+";
        } else if (lowCandle.getId() > highCandle.getId()) {
            direction = "-";
        } else {
            if (currentCandle.getOpen() < currentCandle.getClose()) {
                direction = "+";
            } else if (currentCandle.getOpen() > currentCandle.getClose()) {
                direction = "-";
            }
        }

        String key = StringUtils.joinWith("_", "Candle", pairCodeName);
        String result = String.format("%s %s %s %s%s%%", this.marketName, pairCodeName, currentCandle.getClose(), direction, ratio);
        output(key, result);
    }
}
