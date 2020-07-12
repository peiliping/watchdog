package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.RuleResult;

import java.util.List;


public class OnceRule extends ProxyRule {


    private boolean current;


    public OnceRule(String name, IRule rule) {

        super(name, rule);
        this.current = false;
    }


    public OnceRule(IRule rule) {

        this("OnceRule", rule);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        if (this.current) {
            return true;
        } else {
            this.current = super.rule.match(id, results);
            return this.current;
        }
    }
}
