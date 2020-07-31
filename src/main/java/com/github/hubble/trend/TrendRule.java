package com.github.hubble.trend;


import com.github.hubble.rule.IRule;
import com.github.hubble.rule.ProxyPairRule;
import com.github.hubble.trend.constants.TrendDegree;
import com.github.hubble.trend.constants.TrendType;
import org.apache.commons.lang3.Validate;


public class TrendRule extends ProxyPairRule {


    public TrendRule(String name, IRule leftRule, IRule rightRule) {

        super(name, leftRule, rightRule);
    }


    @Override protected boolean match(long id) {

        super.leftRule.matchRule(id);
        super.rightRule.matchRule(id);
        return true;
    }


    public boolean getTrendTypeResult(TrendType trendType) {

        if (TrendType.UPWARD == trendType) {
            return super.leftRule.isLastResultMatched();
        } else if (TrendType.DOWNWARD == trendType) {
            return super.rightRule.isLastResultMatched();
        }
        Validate.isTrue(false);
        return false;
    }


    public boolean getTrendDegreeResult(TrendDegree trendDegree) {

        if (TrendDegree.POSITIVE == trendDegree) {
            return super.leftRule.isLastResultMatched();
        } else if (TrendDegree.NEGATIVE == trendDegree) {
            return super.rightRule.isLastResultMatched();
        }
        Validate.isTrue(false);
        return false;
    }
}
