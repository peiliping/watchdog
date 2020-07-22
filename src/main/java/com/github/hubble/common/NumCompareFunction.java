package com.github.hubble.common;


public interface NumCompareFunction {


    boolean apply(double first, double second);

    NumCompareFunction GT = (first, second) -> first > second;

    NumCompareFunction GTE = (first, second) -> first >= second;

    NumCompareFunction LT = (first, second) -> first < second;

    NumCompareFunction LTE = (first, second) -> first <= second;

    NumCompareFunction EQ = (first, second) -> first == second;
}
