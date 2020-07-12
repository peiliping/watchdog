package com.github.watchdog.stream;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


@Slf4j
public abstract class AbstractMarketConsumer extends AbstractConsumer {


    protected Map<String, Double> candleChangeRatioConditions = Maps.newHashMap();

    protected double defaultCandleChangeRatioCondition = 2D;

    protected String marketName = "Unknown";


    public AbstractMarketConsumer(String config) {

        if (StringUtils.isNotBlank(config)) {
            log.info("marketConsumer config : " + config);
            JSONObject jsonObject = JSON.parseObject(config);
            JSONObject tJCandleChangeRatioConditions = jsonObject.getJSONObject("candleChangeRatioConditions");
            if (tJCandleChangeRatioConditions != null) {
                for (String key : tJCandleChangeRatioConditions.keySet()) {
                    this.candleChangeRatioConditions.put(key, tJCandleChangeRatioConditions.getDouble(key));
                }
            }
        }
    }
}
