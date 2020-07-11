package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;


public class FusingRule extends IRule {


    private IRule rule;

    private boolean current;


    public FusingRule(String name, IRule rule) {

        super(name);
        this.rule = rule;
        this.current = false;
    }


    @Override public boolean isMatched() {

        if (this.current) {
            return true;
        } else {
            this.current = this.rule.isMatched();
            return this.current;
        }
    }
}
