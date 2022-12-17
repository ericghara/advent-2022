package com.ericgha.monkey;

import com.ericgha.parser.MonkeyBuilder;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Monkey {

    private final int id;
    private final int divisor;
    private final int trueTarget;
    private final int falseTarget;
    private final Function<Integer, Integer> worryFunction;
    private final Queue<Integer> items;
    private int itemsThrown;
    final private int dampening;

    /**
     *
     * @param monkeyBuilder
     * @param dampening
     * @param lcm least common multiple of all monkeys in group
     */
    public Monkey(MonkeyBuilder monkeyBuilder, int dampening, int lcm) {
        this.id = monkeyBuilder.id();
        this.divisor = monkeyBuilder.divisor();
        this.trueTarget = monkeyBuilder.trueTarget();
        this.falseTarget = monkeyBuilder.falseTarget();
        this.worryFunction = monkeyBuilder.WorryFunction(lcm);
        this.items = initItemQueue( monkeyBuilder.items() );
        this.itemsThrown = 0;
        this.dampening = dampening;
        validateTargets();
    }

    private void validateTargets() {
        if (this.trueTarget == this.id || this.falseTarget == this.id) {
            throw new IllegalArgumentException("Monkey cannot pass back to itself");
        }
    }

    private boolean isDivisible(Integer worryScore) {
        return (worryScore % divisor) == 0;
    }

    private void updateItemsThrown() {
        itemsThrown += items.size();
    }

    public TurnResult takeTurn() {
        updateItemsThrown();
        Queue<Integer> trueItems = new ArrayDeque<>();
        Queue<Integer> falseItems = new ArrayDeque<>();
        while (!items.isEmpty() ) {
            Integer score = worryFunction.apply(items.poll()) / dampening;

            if (isDivisible( score ) ) {
                trueItems.offer(score);
            }
            else {
                falseItems.offer(score);
            }
        }
        return new TurnResult( trueItems, falseItems );
    }

    private Queue<Integer> initItemQueue(int[] items) {
        return Arrays.stream( items ).boxed().collect( Collectors.toCollection( ArrayDeque::new ) );
    }

    public int id() {
        return id;
    }

    public int divisor() {
        return divisor;
    }

    public int trueTarget() {
        return trueTarget;
    }

    public int falseTarget() {
        return falseTarget;
    }

    public Function<Integer, Integer> worryFunction() {
        return worryFunction;
    }

    public List<Integer> items() {
        return List.copyOf( items );
    }

    public int itemsThrown() {
        return itemsThrown;
    }

    public void catchItems(Queue<Integer> thrownItems) {
        items.addAll( thrownItems );
    }
}
