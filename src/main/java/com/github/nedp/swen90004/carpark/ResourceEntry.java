package com.github.nedp.swen90004.carpark;

/**
 * Represents an entry point for a resource.
 *
 * Supports operations for reserving the availability of the resource
 * as well as placing an item in it.
 */
interface ResourceEntry<T> {
    /**
     * Puts the item into the resource, relinquishing its availability.
     *
     * Waits until the resource is empty; if availability reservation is
     * used properly, then this method should never wait.
     *
     * @param item the item to be put in the resource.
     * @throws InterruptedException if the thread is interrupted while waitin
     * for the resource to be empty.
     */
    void put(T item) throws InterruptedException;

    /**
     * Waits until the resource is available then reserves its availability.
     *
     * The implementation must guarantee that only one caller may reserve the
     * availability at a time.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the resource to become available.
     */
    void reserveAvailability() throws InterruptedException;
}
