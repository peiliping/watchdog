package com.github.hubble.rule.common;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;


public class PeriodRule extends ProxyRule {


    private long lastId = 0L;


    public PeriodRule(String name, IRule rule) {

        super(name, rule);
    }


    @Override public boolean match(long id) {

        if (super.rule.matchRule(id) && id > this.lastId) {
            this.lastId = id;
            return true;
        }
        return false;
    }
}
