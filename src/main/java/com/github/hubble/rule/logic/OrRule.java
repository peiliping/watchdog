package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;


public class OrRule extends IRule {


    private IRule leftRule;

    private IRule rightRule;


    public OrRule(String name, IRule leftRule, IRule rightRule) {

        super(name);
        this.leftRule = leftRule;
        this.rightRule = rightRule;
    }


    @Override public boolean isMatched() {

        return this.leftRule.isMatched() || this.rightRule.isMatched();
    }
}
