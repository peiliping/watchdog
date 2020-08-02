package com.github.hubble.signal;


import com.github.hubble.common.CandleType;


public interface SignalCallBack {


    void spark(CandleType candleType, Signal signal, String msg);

}
