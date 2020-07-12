package com.github.hubble.rule;


public abstract class ProxyPairRule extends IRule {


    protected IRule leftRule;

    protected IRule rightRule;


    public ProxyPairRule(String name, IRule leftRule, IRule rightRule) {

        super(name);
        this.leftRule = leftRule;
        this.rightRule = rightRule;
    }
}
