package com.github.watchdog.common;


import com.github.hubble.RuleResult;
import com.github.watchdog.stream.MsgChannel;


public class BarkRuleResult extends RuleResult {


    private MsgChannel msgChannel = MsgChannel.getInstance();


    public BarkRuleResult(String message) {

        super(message);
    }


    @Override public void call(long id) {

        this.msgChannel.addResult(super.message);
    }
}
