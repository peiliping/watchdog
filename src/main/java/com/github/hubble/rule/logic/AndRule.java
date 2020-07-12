package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyPairRule;


public class AndRule extends ProxyPairRule {


    public AndRule(String name, IRule leftRule, IRule rightRule) {

        super(name, leftRule, rightRule);
    }


    @Override public boolean isMatched(long id) {

        return super.leftRule.isMatched(id) && super.rightRule.isMatched(id);
    }
}
