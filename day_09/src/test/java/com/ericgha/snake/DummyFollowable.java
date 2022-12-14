package com.ericgha.snake;

import com.ericgha.Location;

public class DummyFollowable extends Followable {

    /**
     * For Testing.  Currently, no way for {@link Follower}s to initialize with this location.
     *
     * @param location start location
     */
    DummyFollowable(Location location) {
        super();
        super.location = location;
    }

}
