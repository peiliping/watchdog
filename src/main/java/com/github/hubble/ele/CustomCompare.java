package com.github.hubble.ele;


public interface CustomCompare<E extends Element> {


    int compareWith(E e1, E e2);

    E delta(E e1, E e2);

    default boolean result(int c) {

        return c > 0;
    }

    default boolean exec(E e1, E e2) {

        return result(compareWith(e1, e2));
    }

    CustomCompare<NumberET> numberETCompare = new CustomCompare<NumberET>() {


        @Override public int compareWith(NumberET e1, NumberET e2) {

            return Double.compare(e1.getData(), e2.getData());
        }


        @Override public NumberET delta(NumberET e1, NumberET e2) {

            return new NumberET(e1.getId(), e1.getData() - e2.getData());
        }
    };
}
