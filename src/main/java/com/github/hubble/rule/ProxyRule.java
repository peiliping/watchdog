package com.github.hubble.rule;


import com.github.hubble.rule.result.RuleResult;


public abstract class ProxyRule extends IRule {


    protected IRule rule;


    public ProxyRule(String name, IRule rule) {

        super(name);
        this.rule = rule;
    }


    public ProxyRule(IRule rule) {

        this("ProxyRule", rule);
    }


    @Override public boolean isMatched(long id) {

        return this.rule.isMatched(id);
    }


    @Override public RuleResult getResult() {

        return this.rule.getResult();
    }
}
