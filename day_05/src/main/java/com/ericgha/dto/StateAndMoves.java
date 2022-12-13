package com.ericgha.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Stream;

public record StateAndMoves(Map<Character, Stack<Character>> state, Stream<CargoMove> moves) {

    public String stateToString() {
        List<Character> keys = new ArrayList<>( state.keySet() );
        keys.sort( Character::compareTo );
        StringBuilder sb = new StringBuilder();
        for (Character key : keys ) {
            sb.append(String.format("%c ", key) );
            sb.append(state.get( key ).toString() );
            sb.append('\n');
        }
        sb.deleteCharAt( sb.length()-1 );
        return sb.toString();
    }

}
