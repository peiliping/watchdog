package com.github.hubble.rule.common;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.rule.RuleResult;

import java.util.List;


public class OnceRule extends ProxyRule {


    private boolean current;


    public OnceRule(String name, IRule rule) {

        super(name, rule);
        this.current = false;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        if (super.rule.matchRule(id, results)) {
            this.current = true;
        }
        return this.current;
    }
}
