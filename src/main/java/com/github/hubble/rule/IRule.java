package com.github.hubble.rule;


public abstract class IRule {


    protected String name;


    public IRule(String name) {

        this.name = name;
    }


    public abstract boolean isMatched();

}
