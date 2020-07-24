package com.github.hubble.rule.common;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.rule.RuleResult;
import com.github.watchdog.common.Util;

import java.util.List;


public class PeriodRule extends ProxyRule {


    private long period;

    private long lastTS = 0L;


    public PeriodRule(String name, IRule rule, long period) {

        super(name, rule);
        this.period = period;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        if (super.rule.matchRule(id, results) && Util.nowSec() - this.lastTS >= this.period) {
            this.lastTS = Util.nowSec();
            return true;
        }
        return false;
    }
}
