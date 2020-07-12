package com.github.watchdog.stream;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.watchdog.common.Util;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


@Slf4j
public abstract class AbstractMarketConsumer extends AbstractConsumer {


    protected Map<String, Long> lastNotifyFilter = Maps.newHashMap();

    protected long notifyInterval = 10 * 60L;

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
}
