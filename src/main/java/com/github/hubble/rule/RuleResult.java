package com.github.hubble.rule;


import com.github.hubble.common.CandleType;
import com.github.watchdog.common.Util;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RuleResult {


    protected final String message;


    public RuleResult(String message) {

        this.message = message;
    }


    public RuleResult(String message, Object... params) {

        this(String.format(message, params));
    }


    public void call(CandleType candleType, long id) {

        if (this.message != null) {
            log.info(Util.timestamp2Date(id) + " " + this.message);
        }
    }
}
