package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;


public class NotRule extends IRule {


    private IRule rule;


    public NotRule(String name, IRule rule) {

        super(name);
        this.rule = rule;
    }


    @Override public boolean isMatched(long id) {

        return !this.rule.isMatched(id);
    }
}
