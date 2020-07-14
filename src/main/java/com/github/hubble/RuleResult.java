package com.github.hubble;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RuleResult {


    protected final String message;


    public RuleResult(String message) {

        this.message = message;
    }


    public void call(long id) {

        if (this.message != null) {
            log.warn(id + " " + this.message);
        }
    }
}
