package com.github.nedp.swen90004.carpark;

/**
 * Represents an exit point for a resource.
 *
 * Supports operations for reserving and getting an item from the resource,
 * as well as making it available for another item.
 */
interface ResourceExit<T> {
    /**
     * Makes the resource available for another item.
     *
     * If the resource is already available, waits until it is unavailable.
     *
     * The caller is must ensure that the resource is only made available if it
     * is empty.
     */
    void makeAvailable() throws InterruptedException;

    /**
     * Retrieves the item from the resource, assuming it is reserved.
     *
     * The caller must guarantee that the item has been reserved and not already
     * retrieved.
     *
     * @return the reserved item.
     */
    T getNow();

    /**
     * Waits until an item is present in the resource, then reserves it
     * without retrieving it.
     *
     * The implementation must guarantee that only one caller may reserve
     * its item at a time.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for an item to enter the resource.
     */
    void reserveItem() throws InterruptedException;
}
