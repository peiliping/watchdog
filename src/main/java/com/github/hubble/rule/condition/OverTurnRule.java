package com.github.hubble.rule.condition;


import com.github.hubble.RuleResult;
import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyRule;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class OverTurnRule extends ProxyRule {


    private boolean current;


    public OverTurnRule(String name, IRule rule, boolean current) {

        super(name, rule);
        this.current = current;
    }


    @Override public boolean match(long id, List<RuleResult> results) {

        boolean last = this.current;
        this.current = super.rule.match(id, results);
        if (log.isDebugEnabled()) {
            log.debug("before {} match : {} .", super.name, this.current);
        }
        return !last && this.current;
    }
}
