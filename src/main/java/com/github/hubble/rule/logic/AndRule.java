package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;


public class AndRule extends IRule {


    private IRule leftRule;

    private IRule rightRule;


    public AndRule(String name, IRule leftRule, IRule rightRule) {

        super(name);
        this.leftRule = leftRule;
        this.rightRule = rightRule;
    }


    @Override public boolean isMatched(long id) {

        return this.leftRule.isMatched(id) && this.rightRule.isMatched(id);
    }
}
