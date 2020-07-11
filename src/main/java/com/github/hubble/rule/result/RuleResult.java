package com.github.hubble.rule.result;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class RuleResult {


    protected String message;


    public RuleResult(String message) {

        this.message = message;
    }


    public void logMsg() {

        if (this.message != null) {
            log.warn(this.message);
        }
    }


    public abstract void call();
}
