package com.github.hubble.common;


import org.apache.commons.lang3.Validate;


public enum CandleType {

    MIN_1(60, 0),
    MIN_5(60 * 5, 0),
    MIN_15(60 * 15, 0),
    MIN_30(60 * 30, 0),
    MIN_60(60 * 60, 0),
    HOUR_4(60 * 60 * 4, 0),
    DAY(60 * 60 * 24, 60 * 60 * 8);


    public long interval;

    public long offset;


    private CandleType(long interval, long offset) {

        this.interval = interval;
        this.offset = offset;
    }


    public static CandleType of(String s) {

        switch (s) {
            case "1min":
                return MIN_1;
            case "5min":
                return MIN_5;
            case "15min":
                return MIN_15;
            case "30min":
                return MIN_30;
            case "60min":
                return MIN_60;
            case "4hour":
                return HOUR_4;
            case "1day":
                return DAY;
        }
        return null;
    }


    public void validate(long timeSeq) {

        Validate.isTrue((timeSeq + this.offset) % this.interval == 0L);
    }
}
