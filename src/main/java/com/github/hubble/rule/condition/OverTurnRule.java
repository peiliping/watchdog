package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;


public class OverTurnRule extends IRule {


    private IRule rule;

    private boolean current;


    public OverTurnRule(String name, IRule rule, boolean current) {

        super(name);
        this.rule = rule;
        this.current = current;
    }


    @Override public boolean isMatched(long id) {

        boolean last = this.current;
        this.current = this.rule.isMatched(id);
        return !last && this.current;
    }
}
