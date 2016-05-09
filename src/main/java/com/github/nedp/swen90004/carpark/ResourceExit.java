package com.github.nedp.swen90004.carpark;

/**
 * Represents an exit point for a resource.
 *
 * Supports operations for taking ownership of the resource and getting an item
 * from it, as well as relinquishing ownership.
 */
interface ResourceExit<T> {

    /**
     * Makes the resource available for another item.
     *
     * If the resource is already available, the implementation should wait
     * until it is unavailable (owned by something).
     * If ownership is respected, then this method should never wait.
     *
     * The caller is must ensure that they own the resource and that it is
     * empty.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the resource to become unavailable.
     */
    void makeAvailable() throws InterruptedException;

    /**
     * Retrieves the item from the resource, assuming it is owned by the caller.
     *
     * The caller must guarantee that they have ownership of the resrouce
     * and that they have not already retrieved the item.
     *
     * The resource is made empty by this method, but ownership is not
     * relinquished.
     *
     * @return the item.
     */
    T getNow();

    /**
     * Waits until an item is present in the resource, then takes ownership
     * of the resource without retrieving it.
     *
     * The implementation must guarantee that only one caller may take
     * ownership at a time.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for an item to enter the resource.
     */
    void acquireWhenFull() throws InterruptedException;
}
