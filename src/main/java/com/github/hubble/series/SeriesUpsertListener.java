package com.github.hubble.series;


import com.github.hubble.ele.Element;


public interface SeriesUpsertListener<E extends Element> {


    void onChange(long seq, E ele, boolean updateOrInsert, Series<E> series);

}
