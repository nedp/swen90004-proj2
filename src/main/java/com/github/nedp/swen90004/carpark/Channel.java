package com.github.nedp.swen90004.carpark;

import java.util.Optional;

/**
 * An abstraction to allow simple synchronized message passing
 * between threads with message content.
 *
 * A Channel may hold zero or one items at any given time.
 *
 * It supports operations for:
 * <ul>
 * <li>sending items through the channel</li>
 * <li>waiting until an item is sent through the channel, then receiving it
 * </li>
 * <li>checking if the channel is currently full and receiving its item if so
 * </li>
 * </ul>
 *
 * These operations are all synchronised on the channel itself.
 *
 * Only one item may be sent through the channel at a time.
 * Subsequent sends must wait until the first item is retrieved.
 * Every item sent through the channel may be retrieved at most once.
 */
class Channel<T> {

    // true if this Channel currently has an item, otherwise false.
    private boolean isFull = false;

    // The item most recently sent through this channel.
    private T item = null;

    /**
     * Waits until an item is available for retrieval from this Channel,
     * then retrieves it.
     *
     * If the channel is already full, this method shall return immediately.
     * Otherwise, this method will wait until the channel is full, then return
     * its contents at that point.
     *
     * @return the first item available for retrieval.
     */
    synchronized T get() throws InterruptedException {
        while (!isFull) {
            wait();
        }
        isFull = false;
        notifyAll();
        return item;
    }

    /**
     * Sends an item through this Channel when the channel has space
     * for it.
     *
     * If the channel is already full, this method shall wait until the
     * channel is no longer full, then send the item.
     * Otherwise, this method will immediately send the item.
     */
    synchronized void put(T item) throws InterruptedException {
        while (isFull) {
            wait();
        }
        this.item = item;
        isFull = true;
        notifyAll();
    }

    /**
     * Attempts to retrieve an item from this Channel without waiting.
     *
     * If an item is ready for retrieval, it is retrieved.
     * Otherwise, an empty Optional is returned immediately.
     *
     * @return Optional.of(item) if an item is ready,
     * otherwise Optional.empty()
     */
    synchronized Optional<T> getIfReady() {
        if (isFull) {
            isFull = false;
            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }
}
