package com.ericgha.snake;

import com.ericgha.Location;

import java.util.ArrayList;
import java.util.List;

public class MirroringLogger extends Followable implements Follower {
    private final List<Location> log;

    public MirroringLogger() {
        super();
        this.log = new ArrayList<>();
    }

    @Override
    public void syncWithLeader(Followable leader) {
        log.add( leader.location() );
        super.location = leader.location();
        super.syncFollower();
    }

    public List<Location> getLog() {
        return log;
    }
}
