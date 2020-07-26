package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.rule.RuleResult;

import java.util.List;


public class NotRule extends ProxyRule {


    public NotRule(String name, IRule rule) {

        super(name, rule);
    }


    @Override public boolean match(long id) {

        return !super.rule.matchRule(id);
    }
}
