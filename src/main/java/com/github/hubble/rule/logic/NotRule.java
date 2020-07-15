package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.RuleResult;

import java.util.List;


public class NotRule extends ProxyRule {


    public NotRule(String name, IRule rule) {

        super(name, rule);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        return !super.rule.matchRule(id, results);
    }
}
