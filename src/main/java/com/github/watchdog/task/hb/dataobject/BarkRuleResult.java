package com.github.watchdog.task.hb.dataobject;


import com.github.hubble.RuleResult;
import com.github.watchdog.stream.MsgChannel;


public class BarkRuleResult extends RuleResult {


    private MsgChannel msgChannel = MsgChannel.getInstance();


    public BarkRuleResult(String message) {

        super(message);
    }


    @Override public void call(long id) {

        super.call(id);
        this.msgChannel.addResult(super.message);
    }
}
