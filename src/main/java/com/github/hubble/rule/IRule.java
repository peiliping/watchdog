package com.github.hubble.rule;


import lombok.Getter;
import lombok.Setter;


public abstract class IRule {


    protected String name;

    @Setter
    @Getter
    protected String msg;


    public IRule(String name) {

        this.name = name;
    }


    public abstract boolean isMatched();

}
