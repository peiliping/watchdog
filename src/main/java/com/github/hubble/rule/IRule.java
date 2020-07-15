package com.github.hubble.rule;


import com.github.hubble.RuleResult;
import com.github.hubble.rule.condition.OnceRule;
import com.github.hubble.rule.condition.OverTurnRule;
import com.github.hubble.rule.condition.PeriodRule;
import com.github.hubble.rule.logic.AndRule;
import com.github.hubble.rule.logic.NotRule;
import com.github.hubble.rule.logic.OrRule;
import com.github.hubble.rule.logic.XorRule;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.List;


@Getter
@Slf4j
public abstract class IRule {


    protected String name;

    @Setter
    protected Class<? extends RuleResult> clazz = RuleResult.class;


    public IRule(String name) {

        this.name = name;
    }


    protected abstract boolean match(long id, List<RuleResult> results);


    public boolean matchRule(long id, List<RuleResult> results) {

        boolean r = match(id, results);
        if (log.isDebugEnabled()) {
            log.debug("{} match : {} .", this.name, r);
        }
        return r;
    }


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

        return new PeriodRule(this.name + "-PeriodRule", this, second);
    }


    public IRule overTurn(boolean initStatus) {

        return new OverTurnRule(this.name + "-OverTurnRule", this, initStatus);
    }


    public IRule once() {

        return new OnceRule(this.name + "-OnceRule", this);
    }


    protected RuleResult createResult(String msg) {

        try {
            return this.clazz.getConstructor(String.class).newInstance(msg);
        } catch (Exception e) {
            log.error("Create Rule Result Error : ", e);
            Validate.isTrue(false, "RuleResult Class Error .");
        }
        return null;
    }
}
