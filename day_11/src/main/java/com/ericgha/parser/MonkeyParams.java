package com.ericgha.parser;

import java.util.Arrays;
import java.util.function.Function;

public class MonkeyParams {

    private int id = Integer.MIN_VALUE;
    private long[] items;

    private Function<Long, Long> worryFunction;

    private int divisor;

    private int trueTarget = Integer.MIN_VALUE;
    private int falseTarget = Integer.MIN_VALUE;

    public int Id() {
        return id;
    }

    public MonkeyParams setId(int id) {
        this.id = id;
        return this;
    }

    public long[] Items() {
        return items;
    }

    public MonkeyParams setItems(long[] items) {
        this.items = items;
        return this;
    }

    public Function<Long, Long> WorryFunction() {
        return worryFunction;
    }

    public MonkeyParams setWorryFunction(Function<Long, Long> worryFunction) {
        this.worryFunction = worryFunction;
        return this;
    }

    public int Divisor() {
        return divisor;
    }

    public MonkeyParams setDivisor(int divisor) {
        this.divisor = divisor;
        return this;
    }

    public int TrueTarget() {
        return trueTarget;
    }

    public MonkeyParams setTrueTarget(int trueTarget) {
        this.trueTarget = trueTarget;
        return this;
    }

    public int FalseTarget() {
        return falseTarget;
    }

    public MonkeyParams setFalseTarget(int falseTarget) {
        this.falseTarget = falseTarget;
        return this;
    }

    @Override
    public String toString() {
        return "com.ericgha.parser.MonkeyParams{" +
                "id=" + id +
                ", items=" + Arrays.toString( items ) +
                ", worryFunction=" + worryFunction +
                ", divisor=" + divisor +
                ", trueTarget=" + trueTarget +
                ", falseTarget=" + falseTarget +
                '}';
    }
}
