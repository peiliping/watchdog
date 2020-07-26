package com.github.hubble.rule;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Set;


public class RulesManager {


    private List<Pair<IRule, RuleResult>> rootRules = Lists.newArrayList();

    private Set<IRule> ruleSet = Sets.newHashSet();


    public void addRule(IRule... rules) {

        for (IRule rule : rules) {
            addRule(rule, null);
        }
    }


    public void addRule(IRule rule, RuleResult ruleResult) {

        checkRef(rule);
        this.rootRules.add(Pair.of(rule, ruleResult));
    }


    private void checkRef(IRule rule) {

        Validate.isTrue(!this.ruleSet.contains(rule), "Rule Ref Error : " + rule.getName());
        this.ruleSet.add(rule);
        List<IRule> refs = rule.getRefRules();
        for (IRule tr : refs) {
            checkRef(tr);
        }
    }


    public void traverseRules(final long id) {

        for (Pair<IRule, RuleResult> rule : this.rootRules) {
            if (rule.getKey().matchRule(id)) {
                if (rule.getValue() != null) {
                    rule.getValue().call(id);
                }
            }
        }
    }
}
