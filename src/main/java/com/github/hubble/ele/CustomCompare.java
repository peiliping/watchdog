package com.github.hubble.ele;


public interface CustomCompare<E extends Element> {


    int compareWith(E e1, E e2);

    default boolean exec(E e1, E e2) {

        return compareWith(e1, e2) > 0;
    }

    CustomCompare<NumberET> numberETCompare = (e1, e2) -> Double.compare(e1.getData(), e2.getData());

    CustomCompare<NumberET> numberETCompareReverse = (e1, e2) -> Double.compare(e2.getData(), e1.getData());
}

