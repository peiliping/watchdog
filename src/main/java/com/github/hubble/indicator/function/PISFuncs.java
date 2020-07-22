package com.github.hubble.indicator.function;


import com.github.hubble.ele.NumberET;

import java.util.function.ToDoubleBiFunction;


public class PISFuncs {


    public static final ToDoubleBiFunction<NumberET, NumberET> PLUS = (numberET, numberET2) -> numberET.getData() + numberET2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> MINUS = (numberET, numberET2) -> numberET.getData() - numberET2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> MULTI = (numberET, numberET2) -> numberET.getData() * numberET2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> DIVIDE = (numberET, numberET2) -> numberET.getData() / numberET2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> PERCENT = (numberET, numberET2) -> numberET.getData() / numberET2.getData() * 100;

}
