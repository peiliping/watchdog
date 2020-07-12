package com.github.hubble.rule.result;


public class PairRuleResult extends RuleResult {


    private RuleResult r1;

    private RuleResult r2;


    public PairRuleResult(RuleResult r1, RuleResult r2) {

        super(null);
        this.r1 = r1;
        this.r2 = r2;
    }


    @Override public void logMsg() {

        this.r1.logMsg();
        this.r2.logMsg();
    }


    @Override public void call() {

        this.r1.call();
        this.r2.call();
    }
}
