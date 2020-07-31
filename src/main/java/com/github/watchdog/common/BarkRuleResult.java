package com.github.watchdog.common;


import com.github.hubble.common.CandleType;
import com.github.hubble.rule.RuleResult;
import com.github.watchdog.stream.MsgChannel;


public class BarkRuleResult extends RuleResult {


    private MsgChannel msgChannel = MsgChannel.getInstance();


    public BarkRuleResult(String message) {

        super(message);
    }


    public BarkRuleResult(String message, Object... params) {

        super(String.format(message, params));
    }


    @Override public void call(CandleType candleType, long id) {

        super.call(candleType, id);
        this.msgChannel.addResult(super.message);
    }
}
