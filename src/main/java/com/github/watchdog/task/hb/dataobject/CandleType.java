package com.github.watchdog.task.hb.dataobject;


public enum CandleType {

    MIN_1(60),
    MIN_5(60 * 5),
    MIN_15(60 * 15),
    MIN_30(60 * 30),
    MIN_60(60 * 60),
    HOUR_4(60 * 60 * 4),
    DAY(60 * 60 * 24),
    WEEK(60 * 60 * 24 * 7);


    public long interval;


    private CandleType(long interval) {

        this.interval = interval;
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
            case "1week":
                return WEEK;
        }
        return null;
    }
}
