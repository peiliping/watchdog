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


    @Override public boolean isMatched() {

        boolean last = this.current;
        this.current = this.rule.isMatched();
        return !last && this.current;
    }
}
