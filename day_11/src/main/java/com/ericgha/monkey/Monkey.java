package com.ericgha.monkey;

import com.ericgha.parser.MonkeyParams;

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
    private final Function<Long, Long> worryFunction;
    private final Queue<Long> items;
    private long itemsThrown;
    private long dampening;

    public Monkey(MonkeyParams monkeyParams, long dampening) {
        this.id = monkeyParams.Id();
        this.divisor = monkeyParams.Divisor();
        this.trueTarget = monkeyParams.TrueTarget();
        this.falseTarget = monkeyParams.FalseTarget();
        this.worryFunction = monkeyParams.WorryFunction();
        this.items = initItemQueue( monkeyParams.Items() );
        this.itemsThrown = 0;
        this.dampening = dampening;
        validateTargets();
    }

    private void validateTargets() {
        if (this.trueTarget == this.id || this.falseTarget == this.id) {
            throw new IllegalArgumentException("Monkey cannot pass back to itself");
        }
    }

    private boolean isDivisible(Long worryScore) {
        return (worryScore % divisor) == 0;
    }

    private void updateItemsThrown() {
        itemsThrown += items.size();
    }

    public TurnResult takeTurn() {
        updateItemsThrown();
        Queue<Long> trueItems = new ArrayDeque<>();
        Queue<Long> falseItems = new ArrayDeque<>();
        while (!items.isEmpty() ) {
            Long score = worryFunction.apply(items.poll()) / dampening;

            if (isDivisible( score ) ) {
                trueItems.offer(score);
            }
            else {
                falseItems.offer(score);
            }
        }
        return new TurnResult( trueItems, falseItems );
    }

    private Queue<Long> initItemQueue(long[] items) {
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

    public Function<Long, Long> worryFunction() {
        return worryFunction;
    }

    public List<Long> items() {
        return List.copyOf( items );
    }

    public long itemsThrown() {
        return itemsThrown;
    }

    public void catchItems(Queue<Long> thrownItems) {
        items.addAll( thrownItems );
    }
}
