package com.github.hubble.rule.result;


public class FixedRuleResult extends RuleResult {


    public FixedRuleResult(String message) {

        super(message);
    }


    @Override public void call() {

        logMsg();
    }
}
