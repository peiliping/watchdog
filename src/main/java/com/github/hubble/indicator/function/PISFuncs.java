package com.github.hubble.indicator.function;


import com.github.hubble.ele.NumberET;

import java.util.function.ToDoubleBiFunction;


public class PISFuncs {


    public static final ToDoubleBiFunction<NumberET, NumberET> PLUS = (num1, num2) -> num1.getData() + num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> MINUS = (num1, num2) -> num1.getData() - num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> MULTI = (num1, num2) -> num1.getData() * num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> DIVIDE = (num1, num2) -> num1.getData() / num2.getData();

    public static final ToDoubleBiFunction<NumberET, NumberET> PERCENT = (num1, num2) -> num1.getData() / num2.getData() * 100;

}
