package com.github.hubble.rule;


import com.github.hubble.common.CandleType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class RulesManager {


    private Map<CandleType, List<Affinity>> rules = Maps.newHashMap();

    private Set<IRule> ruleSet = Sets.newHashSet();


    private void checkRef(IRule rule) {

        Validate.isTrue(!this.ruleSet.contains(rule), "Rule Ref Error : " + rule.getName());
        this.ruleSet.add(rule);
        List<IRule> refs = rule.getRefRules();
        for (IRule tr : refs) {
            checkRef(tr);
        }
    }


    public void addRule(CandleType candleType, Affinity affinity) {

        checkRef(affinity.getRule());
        List<Affinity> affinities = this.rules.get(candleType);
        if (affinities == null) {
            affinities = Lists.newArrayList();
            this.rules.put(candleType, affinities);
        }
        affinities.add(affinity);
    }


    public void traverseRules(final CandleType candleType, final long id) {

        List<Affinity> affinities = this.rules.get(candleType);
        if (affinities == null) {
            return;
        }
        for (Affinity affinity : affinities) {
            if (affinity.getRule().matchRule(id)) {
                affinity.getResult().call(id);
            }
        }
    }
}
