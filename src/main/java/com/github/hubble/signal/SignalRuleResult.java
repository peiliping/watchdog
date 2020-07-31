package com.github.hubble.signal;


import com.github.hubble.common.CandleType;
import com.github.hubble.rule.RuleResult;


public class SignalRuleResult extends RuleResult {


    protected final Signal signal;

    protected final SignalCallBack callBack;


    public SignalRuleResult(String msg, Signal signal, SignalCallBack callBack) {

        super(msg);
        this.signal = signal;
        this.callBack = callBack;
    }


    @Override
    public void call(CandleType candleType, long id) {

        super.call(candleType, id);
        this.callBack.spark(candleType, this.signal);
    }
}
