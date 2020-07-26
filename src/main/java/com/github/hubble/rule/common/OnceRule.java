package com.github.hubble.rule.common;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;


public class OnceRule extends ProxyRule {


    private boolean current;


    public OnceRule(String name, IRule rule) {

        super(name, rule);
        this.current = false;
    }


    @Override public boolean match(long id) {

        if (super.rule.matchRule(id)) {
            this.current = true;
        }
        return this.current;
    }
}
