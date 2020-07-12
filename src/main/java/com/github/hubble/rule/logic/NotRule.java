package com.github.hubble.rule.logic;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;


public class NotRule extends ProxyRule {


    public NotRule(String name, IRule rule) {

        super(name, rule);
    }


    @Override public boolean isMatched(long id) {

        return !super.rule.isMatched(id);
    }
}
