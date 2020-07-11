package com.github.hubble.rule.result;


public class OnceRuleResult extends RuleResult {


    public OnceRuleResult(String message) {

        super(message);
    }


    @Override public void call() {

        logMsg();
        super.message = null;
    }
}
