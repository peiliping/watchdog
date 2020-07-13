package com.github.hubble;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RuleResult {


    protected String message;


    public RuleResult(String message) {

        this.message = message;
    }


    public void logMsg(long id) {

        if (this.message != null) {
            log.warn(id + " " + this.message);
        }
    }


    public void call(long id) {

        logMsg(id);
    }
}
