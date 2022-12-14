package com.ericgha.snake;

public interface Follower {


    /**
     * Signal to synchronize with the leader.  Designed to be called by the {@link Followable} leading this segment.
     * Typically, called when the {@code leader} updates its position.
     *
     * @param leader - {@code Followable} leading this segment.
     */
    void syncWithLeader(Followable leader);

}
