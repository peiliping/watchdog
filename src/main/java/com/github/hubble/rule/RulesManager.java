package com.github.hubble.rule;


import com.google.common.collect.Lists;

import java.util.List;


public class RulesManager {


    private List<IRule> rules = Lists.newArrayList();


    public void addRule(IRule rule) {

        this.rules.add(rule);
    }


    public void traverseRules() {

        for (IRule rule : this.rules) {
            rule.isMatched();
        }
    }

}
