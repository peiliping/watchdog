package com.github.hubble.rule;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class RulesManager {


    private boolean lock = false;

    private List<IRule> rules = Lists.newArrayList();


    public void addRule(IRule... rules) {

        if (this.lock) {
            return;
        }
        for (IRule rule : rules) {
            this.rules.add(rule);
        }
        this.lock = true;
    }


    public void traverseRules(long id) {

        for (IRule rule : this.rules) {
            if (rule.isMatched(id)) {
                rule.getResult().call();
            }
        }
    }
}
