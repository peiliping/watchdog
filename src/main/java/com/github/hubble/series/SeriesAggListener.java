package com.github.hubble.series;


import com.github.hubble.ele.CandleET;
import org.apache.commons.lang3.Validate;


public class SeriesAggListener implements SeriesUpsertListener<CandleET> {


    private final CandleSeries candleSeries;

    private CandleET candleET;

    private CandleET lastCandle;


    public SeriesAggListener(CandleSeries candleSeries) {

        this.candleSeries = candleSeries;
    }


    @Override public void onChange(long seq, CandleET ele, boolean updateOrInsert, Series<CandleET> series) {

        long tid = this.candleSeries.candleType.convert(ele.getId());
        if (this.candleET == null || this.candleET.getId() < tid) {
            this.candleET = new CandleET(tid, ele.getOpen(), ele.getLow(), ele.getHigh(), ele.getClose(), ele.getAmount(), ele.getVolume(), ele.getCount());
        } else if (this.candleET.getId() == tid) {
            if (this.lastCandle.getId() < ele.getId()) {
                this.candleET = new CandleET(tid, this.candleET.getOpen(),
                                             Math.min(this.candleET.getLow(), ele.getLow()),
                                             Math.max(this.candleET.getHigh(), ele.getHigh()),
                                             ele.getClose(),
                                             this.candleET.getAmount() + ele.getAmount(),
                                             this.candleET.getVolume() + ele.getVolume(),
                                             this.candleET.getCount() + ele.getCount());
            } else if (this.lastCandle.getId() == ele.getId()) {
                this.candleET = new CandleET(tid, this.candleET.getOpen(),
                                             Math.min(this.candleET.getLow(), ele.getLow()),
                                             Math.max(this.candleET.getHigh(), ele.getHigh()),
                                             ele.getClose(),
                                             this.candleET.getAmount() + ele.getAmount() - this.lastCandle.getAmount(),
                                             this.candleET.getVolume() + ele.getVolume() - this.lastCandle.getAmount(),
                                             this.candleET.getCount() + ele.getCount() - this.lastCandle.getCount());
            } else {
                Validate.isTrue(false);
            }
        } else {
            Validate.isTrue(false);
        }
        this.lastCandle = ele;
        this.candleSeries.add(this.candleET);
    }
}
