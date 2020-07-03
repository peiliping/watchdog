package com.github.watchdog.task.kb.dataobject;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PushMsg {


    private String event;

    private Boolean success;

    private String biz;

    private String type;

    private String pairCode;

    private String interval;

    private String step;

    private Boolean zip;

    private String data;

    private Long userId;

    private long ts;
}
