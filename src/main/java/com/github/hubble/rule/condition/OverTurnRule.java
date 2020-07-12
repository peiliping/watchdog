package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.RuleResult;

import java.util.List;


public class OverTurnRule extends ProxyRule {


    private boolean current;


    public OverTurnRule(String name, IRule rule, boolean current) {

        super(name, rule);
        this.current = current;
    }


    public OverTurnRule(IRule rule, boolean current) {

        this("OverTurnRule", rule, current);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        boolean last = this.current;
        this.current = super.rule.match(id, results);
        return !last && this.current;
    }
}
