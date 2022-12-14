package com.ericgha.snake;

import com.ericgha.Location;

import java.util.HashSet;
import java.util.Set;

public class BodySegment extends Followable implements Follower {

    private final Set<Location> seen;

    public BodySegment() {
        super();
        this.seen = new HashSet<>();
        this.seen.add( super.location );
    }

    private int getMove(int delta) {
        if (-1 <= delta && delta <= 1) {
            return delta;
        }
        return delta / 2;
    }

    /**
     * @param leader Location of the {@link Followable} leading this instance. For example a {@link Head} OR
     *               {@link BodySegment}
     */
    @Override
    public void syncWithLeader(Followable leader) {
        Location curLeadLocation = leader.location();
        int distSq = super.location.distanceSq( curLeadLocation );
        if (distSq <= 2) {
            return;
        }
        int deltaX = curLeadLocation.x() - super.location.x();
        int deltaY = curLeadLocation.y() - super.location.y();
        int moveX = getMove( deltaX );
        int moveY = getMove( deltaY );
        super.location = new Location( super.location.x() + moveX, super.location.y() + moveY );
        seen.add( super.location );
        super.syncFollower();
    }

    public Set<Location> getSeen() {
        return Set.copyOf( seen );
    }

}
