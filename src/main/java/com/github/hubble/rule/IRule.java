package com.github.hubble.rule;


import com.github.hubble.rule.common.OnceRule;
import com.github.hubble.rule.common.OverTurnRule;
import com.github.hubble.rule.common.PeriodRule;
import com.github.hubble.rule.logic.AndRule;
import com.github.hubble.rule.logic.NotRule;
import com.github.hubble.rule.logic.OrRule;
import com.github.hubble.rule.logic.XorRule;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Getter
@Slf4j
public abstract class IRule {


    protected String name;

    private boolean lastResultMatched = false;


    public IRule(String name) {

        this.name = name;
    }


    public List<IRule> getRefRules() {

        return Lists.newArrayList();
    }


    protected boolean prepare(long id) {

        return true;
    }


    protected abstract boolean match(long id);


    public final boolean matchRule(long id) {

        if (!prepare(id)) {
            this.lastResultMatched = false;
        } else {
            this.lastResultMatched = match(id);
        }
        if (log.isDebugEnabled()) {
            log.debug("{} match : {} .", this.name, this.lastResultMatched);
        }
        return this.lastResultMatched;
    }


    //逻辑运算
    public IRule and(IRule rule) {

        String t = String.format("AndRule[%s,%s]", this.name, rule.name);
        return new AndRule(t, this, rule);
    }


    public IRule or(IRule rule) {

        String t = String.format("OrRule[%s,%s]", this.name, rule.name);
        return new OrRule(t, this, rule);
    }


    public IRule xor(IRule rule) {

        String t = String.format("XorRule[%s,%s]", this.name, rule.name);
        return new XorRule(t, this, rule);
    }


    public IRule not() {

        return new NotRule(this.name + "-NotRule", this);
    }


    //常用组合
    public IRule period(long second) {

        return new PeriodRule(this.name + "-PeriodRule", this, second);
    }


    public IRule overTurn(boolean initStatus) {

        return new OverTurnRule(this.name + "-OverTurnRule", this, initStatus);
    }


    public IRule once() {

        return new OnceRule(this.name + "-OnceRule", this);
    }
}
