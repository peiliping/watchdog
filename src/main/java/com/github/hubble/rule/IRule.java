package com.github.hubble.rule;


import com.github.hubble.rule.logic.AndRule;
import com.github.hubble.rule.logic.NotRule;
import com.github.hubble.rule.logic.OrRule;
import com.github.hubble.rule.logic.XorRule;
import com.github.hubble.rule.result.RuleResult;
import lombok.Getter;
import lombok.Setter;


public abstract class IRule {


    protected String name;

    @Getter
    @Setter
    protected RuleResult result;


    public IRule(String name) {

        this.name = name;
    }


    public abstract boolean isMatched(long id);


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

}
