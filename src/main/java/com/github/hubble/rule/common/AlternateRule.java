package com.github.hubble.rule.common;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.IRuleOb;
import com.github.hubble.rule.ProxyRule;
import com.github.hubble.rule.RuleResult;

import java.util.List;


public class AlternateRule extends ProxyRule {


    private IRuleOb ruleOb;

    private boolean op = true;


    public AlternateRule(String name, IRule rule, IRuleOb ruleOb) {

        super(name, rule);
        this.ruleOb = ruleOb;
    }


    @Override protected boolean match(long id, List<RuleResult> results) {

        boolean result = super.rule.matchRule(id, results);
        if (this.ruleOb.getLastMatchResult()) {
            this.op = true;
        }
        if (result && this.op) {
            this.op = false;
            return true;
        }
        return false;
    }
}
