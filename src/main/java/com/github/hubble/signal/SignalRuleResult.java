package com.github.hubble.signal;


import com.github.hubble.rule.RuleResult;


public class SignalRuleResult extends RuleResult {


    protected final Signal signal;


    public SignalRuleResult(Signal signal) {

        super(null);
        this.signal = signal;
    }


    public void call(long id) {

        super.call(id);
    }
}
