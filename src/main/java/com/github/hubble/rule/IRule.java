package com.github.hubble.rule;


public abstract class IRule {


    private String name;


    public IRule(String name) {

        this.name = name;
    }


    public abstract boolean isMatched();

}
