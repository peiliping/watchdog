package com.github.hubble;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RuleResult {


    protected String message;


    public RuleResult(String message) {

        this.message = message;
    }


    public void logMsg() {

        if (this.message != null) {
            log.warn(this.message);
        }
    }


    public void call() {

        logMsg();
    }
}
