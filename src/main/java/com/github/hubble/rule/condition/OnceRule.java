package com.github.hubble.rule.condition;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;


public class OnceRule extends ProxyRule {


    private boolean current;


    public OnceRule(String name, IRule rule) {

        super(name, rule);
        this.current = false;
    }


    public OnceRule(IRule rule) {

        this("OnceRule", rule);
    }


    @Override public boolean isMatched(long id) {

        if (this.current) {
            return true;
        } else {
            this.current = super.rule.isMatched(id);
            return this.current;
        }
    }
}
