package com.github.hubble.rule;


import lombok.Getter;


@Getter
public class Affinity {


    private IRule rule;

    private RuleResult result;


    public Affinity(IRule rule, RuleResult result) {

        this.rule = rule;
        this.result = result;
    }
}
