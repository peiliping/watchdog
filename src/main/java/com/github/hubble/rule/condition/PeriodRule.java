package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.RuleResult;
import com.github.watchdog.common.Util;

import java.util.List;


public class PeriodRule extends ProxyRule {


    private long period;

    private long lastTS = 0L;


    public PeriodRule(String name, IRule rule, long period) {

        super(name, rule);
        this.period = period;
    }


    public PeriodRule(IRule rule, long period) {

        this("PeriodRule", rule, period);
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        if (Util.nowSec() - this.lastTS < this.period) {
            return false;
        }
        if (super.rule.match(id, results)) {
            this.lastTS = Util.nowSec();
            return true;
        } else {
            return false;
        }
    }
}
