package com.github.hubble.rule;


import com.github.hubble.rule.condition.AlternateRule;
import com.github.hubble.rule.condition.OnceRule;
import com.github.hubble.rule.condition.OverTurnRule;
import com.github.hubble.rule.condition.PeriodRule;
import com.github.hubble.rule.logic.AndRule;
import com.github.hubble.rule.logic.NotRule;
import com.github.hubble.rule.logic.OrRule;
import com.github.hubble.rule.logic.XorRule;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

import java.util.List;


@Getter
@Slf4j
public abstract class IRule implements IRuleOb {


    protected String name;

    private boolean lastMatchResult = false;

    @Setter
    protected Class<? extends RuleResult> clazz = RuleResult.class;


    public IRule(String name) {

        this.name = name;
    }


    public List<IRule> getRefRules() {

        return Lists.newArrayList();
    }


    protected boolean prepare(long id, List<RuleResult> results) {

        return true;
    }


    protected abstract boolean match(long id, List<RuleResult> results);


    public final boolean matchRule(long id, List<RuleResult> results) {

        if (!prepare(id, results)) {
            this.lastMatchResult = false;
        } else {
            this.lastMatchResult = match(id, results);
        }
        if (log.isDebugEnabled()) {
            log.debug("{} match : {} .", this.name, this.lastMatchResult);
        }
        return this.lastMatchResult;
    }


    @Override public boolean getLastMatchResult() {

        return this.lastMatchResult;
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


    public IRule alternateRule(IRuleOb ob) {

        return new AlternateRule(this.name + "-AlternateRule", this, ob);
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
