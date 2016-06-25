package com.github.nedp.swen90004.carpark;

/**
 * An interface for objects which may be waited on until they are 'free'.
 */
interface Freeable {
    /**
     * Waits until this object is free.
     * The implementation must ensure this check is synchronised appropriately,
     * but this method itself should not have any locking semantics.
     */
    void waitUntilFree() throws InterruptedException;
}
