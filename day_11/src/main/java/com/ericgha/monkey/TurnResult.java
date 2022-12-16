package com.ericgha.monkey;

import java.util.Queue;

record TurnResult(Queue<Long> trueItems, Queue<Long> falseItems) {

}
