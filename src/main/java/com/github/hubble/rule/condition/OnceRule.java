package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;


public class OnceRule extends IRule {


    private IRule rule;

    private boolean current;


    public OnceRule(String name, IRule rule) {

        super(name);
        this.rule = rule;
        this.current = false;
    }


    @Override public boolean isMatched(long id) {

        if (this.current) {
            return true;
        } else {
            this.current = this.rule.isMatched(id);
            return this.current;
        }
    }
}
