package com.github.hubble.rule.condition;


import com.github.hubble.RuleResult;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;

import java.util.List;


public class OnceRule extends ProxyRule {


    private boolean current;


    public OnceRule(String name, IRule rule) {

        super(name, rule);
        this.current = false;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        if (this.current) {
            return true;
        } else {
            this.current = super.rule.matchRule(id, results);
            return this.current;
        }
    }
}
