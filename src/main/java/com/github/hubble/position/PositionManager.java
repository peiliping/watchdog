package com.github.hubble.position;


import com.github.hubble.ele.CandleET;
import com.github.hubble.series.Series;
import com.github.hubble.series.SeriesUpsertListener;
import com.github.hubble.signal.OperateSignal;


public class PositionManager implements SeriesUpsertListener<CandleET> {


    public void handleSignal(OperateSignal signal, int ratio) {
        //买入卖出
    }


    @Override public void onChange(long seq, CandleET ele, boolean updateOrInsert, Series<CandleET> series) {
        //止损止盈计算
    }
}
