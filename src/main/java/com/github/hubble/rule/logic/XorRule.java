package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyPairRule;
import com.github.hubble.rule.RuleResult;

import java.util.List;


public class XorRule extends ProxyPairRule {


    public XorRule(String name, IRule leftRule, IRule rightRule) {

        super(name, leftRule, rightRule);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        return super.leftRule.matchRule(id, results) ^ super.rightRule.matchRule(id, results);
    }
}
