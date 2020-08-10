package com.github.hubble.rule;


import lombok.Getter;


@Getter
public class Affinity {


    private final IRule rule;

    private final RuleResult result;


    public Affinity(IRule rule, RuleResult result) {

        this.rule = rule;
        this.result = result;
    }
}
