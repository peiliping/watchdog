package com.github.hubble.rule;


import com.google.common.collect.Lists;

import java.util.List;


public abstract class ProxyRule extends IRule {


    protected IRule rule;


    public ProxyRule(String name, IRule rule) {

        super(name);
        this.rule = rule;
    }


    @Override public List<IRule> getRefRules() {

        return Lists.newArrayList(this.rule);
    }
}
