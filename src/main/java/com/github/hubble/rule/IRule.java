package com.github.hubble.rule;


import com.github.hubble.RuleResult;
import com.github.hubble.rule.condition.OverTurnRule;
import com.github.hubble.rule.condition.PeriodRule;
import com.github.hubble.rule.logic.AndRule;
import com.github.hubble.rule.logic.NotRule;
import com.github.hubble.rule.logic.OrRule;
import com.github.hubble.rule.logic.XorRule;
import lombok.Getter;

import java.util.List;


@Getter
public abstract class IRule {


    protected String name;


    public IRule(String name) {

        this.name = name;
    }


    public abstract boolean match(long id, List<RuleResult> results);


    //逻辑运算
    public IRule and(IRule rule) {

        return new AndRule("AndRule", this, rule);
    }


    public IRule or(IRule rule) {

        return new OrRule("OrRule", this, rule);
    }


    public IRule xor(IRule rule) {

        return new XorRule("XorRule", this, rule);
    }


    public IRule not() {

        return new NotRule("NotRule", this);
    }


    //常用组合
    public IRule period(long second) {

        return new PeriodRule("PeriodRule", this, second);
    }


    public IRule overTurn(boolean initStatus) {

        return new OverTurnRule("OverTurnRule", this, initStatus);
    }
}
