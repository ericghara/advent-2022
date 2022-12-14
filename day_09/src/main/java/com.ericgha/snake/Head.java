package com.ericgha.snake;

import com.ericgha.Location;
import com.ericgha.move.Move;
import com.ericgha.move.X;
import com.ericgha.move.Y;

import java.util.function.Function;
import java.util.stream.Stream;

public class Head extends Followable {

    public Head() {
        super();
    }

    void step(Move move) {
        Function<Location, Location> stepFunc = getStepFunction( move );
        for (int step = 0; step < Math.abs( move.distance() ); step++) {
            super.location = stepFunc.apply( super.location );
            super.syncFollower();
        }
    }

    private Function<Location, Location> getStepFunction(Move move) {
        if (move instanceof X) {
            return xMove( (X) move );
        }
        if (move instanceof Y) {
            return yMove( (Y) move );
        }
        throw new IllegalArgumentException( "Unrecognized move" );
    }

    private Function<Location, Location> xMove(X yMove) {
        int step = yMove.distance() >= 0 ? 1 : -1;
        return (loc) -> new Location( loc.x() + step, loc.y() );
    }

    private Function<Location, Location> yMove(Y yMove) {
        int step = yMove.distance() >= 0 ? 1 : -1;
        return (loc) -> new Location( loc.x(), loc.y() + step );
    }

    public void move(Stream<Move> moves) {
        moves.forEach( this::step );
    }

}
