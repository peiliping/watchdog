package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyPairRule;


public class OrRule extends ProxyPairRule {


    public OrRule(String name, IRule leftRule, IRule rightRule) {

        super(name, leftRule, rightRule);
    }


    @Override public boolean match(long id) {

        return super.leftRule.matchRule(id) | super.rightRule.matchRule(id);
    }
}
