package com.github.watchdog.stream;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public abstract class AbstractMarketConsumer extends AbstractConsumer {


    protected String marketName = "Unknown";


    public AbstractMarketConsumer(String config) {

        if (StringUtils.isNotBlank(config)) {
            log.info("marketConsumer config : " + config);
            JSONObject jsonObject = JSON.parseObject(config);
        }
    }
}
