package com.github.watchdog.task.hb.dataobject;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PushMsg {


    private String ch;

    private long ts;

    private String tick;
}
