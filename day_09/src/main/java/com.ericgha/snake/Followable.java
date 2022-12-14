package com.ericgha.snake;

import com.ericgha.Location;

import java.util.Objects;

/**
 * All snake segments should extend.
 */
public abstract class Followable {

    private Follower follower;
    Location location;

    public Followable() {
        this.location = new Location( 0, 0 );
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }

    public Location location() {
        return location;
    }

    /**
     * Sync with the {@link Follower} (if any).  Typically, called
     * when the location of this {@code Followable} changes.
     */
    void syncFollower() {
        if (Objects.isNull( follower ) ) {
            return;
        }
        follower.syncWithLeader( this );
    }
}
