package com.github.hubble.rule;


import com.github.hubble.RuleResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;


@Slf4j
public class RulesManager {


    private List<Pair<IRule, RuleResult>> rootRules = Lists.newArrayList();


    public void addRule(IRule... rules) {

        for (IRule rule : rules) {
            this.rootRules.add(Pair.of(rule, null));
        }
    }


    public void addRule(IRule rule, RuleResult ruleResult) {

        this.rootRules.add(Pair.of(rule, ruleResult));
    }


    public void traverseRules(final long id) {

        for (Pair<IRule, RuleResult> rule : this.rootRules) {
            List<RuleResult> results = Lists.newArrayList();
            if (rule.getKey().match(id, results)) {
                if (log.isDebugEnabled()) {
                    log.debug("{} match : true .", rule.getKey().name);
                }
                results.forEach(ruleResult -> ruleResult.call(id));
                if (rule.getValue() != null) {
                    rule.getValue().call(id);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("{} match : false .", rule.getKey().name);
                }
            }
        }
    }
}
