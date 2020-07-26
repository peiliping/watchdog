package com.github.hubble.rule.common;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.watchdog.common.Util;


public class PeriodRule extends ProxyRule {


    private long period;

    private long lastTS = 0L;


    public PeriodRule(String name, IRule rule, long period) {

        super(name, rule);
        this.period = period;
    }


    @Override public boolean match(long id) {

        if (super.rule.matchRule(id) && Util.nowSec() - this.lastTS >= this.period) {
            this.lastTS = Util.nowSec();
            return true;
        }
        return false;
    }
}
