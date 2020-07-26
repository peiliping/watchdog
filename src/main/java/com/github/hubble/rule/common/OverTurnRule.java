package com.github.hubble.rule.common;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;


public class OverTurnRule extends ProxyRule {


    private boolean current;


    public OverTurnRule(String name, IRule rule, boolean current) {

        super(name, rule);
        this.current = current;
    }


    @Override public boolean match(long id) {

        boolean last = this.current;
        this.current = super.rule.matchRule(id);
        return !last && this.current;
    }
}
