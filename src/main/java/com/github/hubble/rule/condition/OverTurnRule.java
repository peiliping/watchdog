package com.github.hubble.rule.condition;


import com.github.hubble.rule.RuleResult;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;

import java.util.List;


public class OverTurnRule extends ProxyRule {


    private boolean current;


    public OverTurnRule(String name, IRule rule, boolean current) {

        super(name, rule);
        this.current = current;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        boolean last = this.current;
        this.current = super.rule.matchRule(id, results);
        return !last && this.current;
    }
}
