package com.ericgha.parser;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MonkeyBuilder {

    private int id = Integer.MAX_VALUE;
    private int[] items;

    private Function<Integer, Integer> worryFunction;

    private int divisor;

    private int trueTarget = Integer.MAX_VALUE;
    private int falseTarget = Integer.MAX_VALUE;
    private String var0 = null;
    private String var1 = null;
    private char operation = Character.MAX_VALUE;

    public int id() {
        return id;
    }

    public MonkeyBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public int[] items() {
        return items;
    }

    public MonkeyBuilder setItems(int[] items) {
        this.items = items;
        return this;
    }

    public Function<Integer, Integer> WorryFunction(int leastCommonMultiple) {
        final String OLD_KEY = "old";
        BiFunction<Integer, Integer,Integer> opFunction = switch (operation) {
            case '+' -> (a,b) -> (a+b)%leastCommonMultiple;
            case '*' -> (a,b) -> (int) ( (long) a*b%leastCommonMultiple);
            default -> throw new IllegalArgumentException( "Unrecognized operation char: " + operation );
        };
        if (var0.equals( OLD_KEY ) && var1.equals( OLD_KEY )) {
            return (old) -> opFunction.apply( old, old );
        }
        int arg;
        if (var0.equals( OLD_KEY )) {
            arg = Integer.parseInt( var1 );
        } else if (var1.equals( OLD_KEY )) {
            arg = Integer.parseInt( var0 );
        } else {
            throw new IllegalArgumentException( String.format( "Unrecognized input vars: %s, %s", var0, var1 ) );
        }
        return (old) -> opFunction.apply( old, arg );
    }

    /**
     * @param var0      {@code OLD_KEY} or a string representation of a number
     * @param var1      {@code OLD_KEY} or a string representation of a number
     * @param operation '*' or '+'
     * @return this
     */
    public MonkeyBuilder setWorryParams(String var0, String var1, char operation) {
        this.var0 = var0;
        this.var1 = var1;
        this.operation = operation;
        return this;
    }

    public int divisor() {
        return divisor;
    }

    public MonkeyBuilder setDivisor(int divisor) {
        this.divisor = divisor;
        return this;
    }

    public int trueTarget() {
        return trueTarget;
    }

    public MonkeyBuilder setTrueTarget(int trueTarget) {
        this.trueTarget = trueTarget;
        return this;
    }

    public int falseTarget() {
        return falseTarget;
    }

    public MonkeyBuilder setFalseTarget(int falseTarget) {
        this.falseTarget = falseTarget;
        return this;
    }

    @Override
    public String toString() {
        return "com.ericgha.parser.MonkeyBuilder{" +
                "id=" + id +
                ", items=" + Arrays.toString( items ) +
                ", worryFunction=" + worryFunction +
                ", divisor=" + divisor +
                ", trueTarget=" + trueTarget +
                ", falseTarget=" + falseTarget +
                '}';
    }

    private static Integer product(Integer multiplicand, Integer multiplier) {
        return multiplicand * multiplier;
    }
}
