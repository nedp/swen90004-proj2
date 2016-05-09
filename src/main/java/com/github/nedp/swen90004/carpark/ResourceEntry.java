package com.github.nedp.swen90004.carpark;

/**
 * Represents an entry point for a resource.
 *
 * Supports operations for taking ownership of the resource and putting an item
 * into it which relinquishes ownership.
 */
interface ResourceEntry<T> {
    /**
     * Puts the item into the resource, relinquishing ownership of the resource.
     *
     * Waits until the resource is empty; if ownership is respected,
     * then this method should never wait.
     *
     * @param item the item to be put in the resource.
     * @throws InterruptedException if the thread is interrupted while waitin
     * for the resource to be empty.
     */
    void put(T item) throws InterruptedException;

    /**
     * Waits until the resource is available then takes ownership of it.
     *
     * The implementation must guarantee that only one caller may take ownership
     * of the resource at a time.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the resource to become available.
     */
    void acquireWhenEmpty() throws InterruptedException;
}
