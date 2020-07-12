package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;
import com.github.watchdog.common.Util;


public class PeriodRule extends IRule {


    private IRule rule;

    private long period;

    private long lastTS = 0L;


    public PeriodRule(String name, IRule rule, long period) {

        super(name);
        this.rule = rule;
        this.period = period;
    }


    public PeriodRule(IRule rule, long period) {

        this("PeriodRule", rule, period);
    }


    @Override public boolean isMatched(long id) {

        if (Util.nowSec() - this.lastTS < this.period) {
            return false;
        }
        if (this.rule.isMatched(id)) {
            this.lastTS = Util.nowSec();
            return true;
        } else {
            return false;
        }
    }
}
