package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;


public class OverTurnRule extends IRule {


    private IRule rule;

    private boolean current = false;


    public OverTurnRule(String name, IRule rule) {

        super(name);
        this.rule = rule;
    }


    @Override public boolean isMatched() {

        if (this.current) {
            this.current = this.rule.isMatched();
            return false;
        } else {
            this.current = this.rule.isMatched();
            return this.current;
        }
    }
}
