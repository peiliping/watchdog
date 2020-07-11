package com.github.hubble.rule;


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


    public abstract boolean isMatched();

}
