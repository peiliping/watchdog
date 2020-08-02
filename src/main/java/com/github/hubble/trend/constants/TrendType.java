package com.github.hubble.trend.constants;


public enum TrendType {

    UPWARD("UP"),
    SHOCK("SK"),
    DOWNWARD("DOWN");

    public String name;


    private TrendType(String n) {

        this.name = n;
    }
}
