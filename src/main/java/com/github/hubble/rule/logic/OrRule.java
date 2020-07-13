package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyPairRule;
import com.github.hubble.RuleResult;

import java.util.List;


public class OrRule extends ProxyPairRule {


    public OrRule(String name, IRule leftRule, IRule rightRule) {

        super(name, leftRule, rightRule);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        return super.leftRule.match(id, results) | super.rightRule.match(id, results);
    }
}
