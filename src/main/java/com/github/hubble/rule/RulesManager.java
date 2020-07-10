package com.github.hubble.rule;


import com.github.hubble.ele.CandleET;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class RulesManager {


    private List<IRule> rules = Lists.newArrayList();


    public void addRule(IRule rule) {

        this.rules.add(rule);
    }


    public void traverseRules(CandleET candleET) {

        for (IRule rule : this.rules) {
            if (rule.isMatched()) {
                log.info(candleET.toString());
                log.warn(rule.getMsg());
            }
        }
    }

}
