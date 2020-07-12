package com.github.hubble.rule;


import com.github.hubble.rule.result.PairRuleResult;
import com.github.hubble.rule.result.RuleResult;


public abstract class ProxyPairRule extends IRule {


    protected IRule leftRule;

    protected IRule rightRule;


    public ProxyPairRule(String name, IRule leftRule, IRule rightRule) {

        super(name);
        this.leftRule = leftRule;
        this.rightRule = rightRule;
    }


    @Override public RuleResult getResult() {

        return new PairRuleResult(this.leftRule.result, this.rightRule.result);
    }
}
