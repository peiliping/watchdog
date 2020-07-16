package com.github.hubble.rule;


import com.google.common.collect.Lists;

import java.util.List;


public abstract class ProxyPairRule extends IRule {


    protected IRule leftRule;

    protected IRule rightRule;


    public ProxyPairRule(String name, IRule leftRule, IRule rightRule) {

        super(name);
        this.leftRule = leftRule;
        this.rightRule = rightRule;
    }


    @Override public List<IRule> getRefRules() {

        return Lists.newArrayList(this.leftRule, this.rightRule);
    }
}
