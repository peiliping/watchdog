package com.github.hubble.rule;


public abstract class ProxyRule extends IRule {


    protected IRule rule;


    public ProxyRule(String name, IRule rule) {

        super(name);
        this.rule = rule;
    }
}
