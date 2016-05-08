package com.github.nedp.swen90004.carpark;

import java.util.Optional;

/**
 * An abstraction to allow simple synchronized message passing
 * between threads without message content.
 *
 * A NullChannel doesn't hold items, but does conceptually hold empty messages.
 * This class is required (can't just use something like {@link Channel<null>})
 * because of limitations in Java's type system.
 *
 * It supports operations for:
 * <ul>
 * <li>sending a message through the channel</li>
 * <li>waiting until a message is sent through the channel, then receiving it
 * </li>
 * <li>checking if the channel currently has a message and receiving it if so
 * </li>
 * </ul>
 *
 * These operations are all synchronised on the channel itself.
 *
 * Only one message may be sent through the channel at a time.
 * Subsequent sends must wait until the first message is received.
 * Every message sent through the channel may be received at most once.
 */
class NullChannel {

    // true if this Channel currently has a message, otherwise false.
    private boolean isFull = false;

    /**
     * Waits until a message is available for retrieval from this Channel,
     * then retrieves it.
     *
     * If the channel is already full, this method shall return immediately.
     * Otherwise, this method will wait until the channel is full.
     *
     * Since the messages of this channel are empty, this returns nothing.
     */
    synchronized void get() throws InterruptedException {
        while (!isFull) {
            wait();
        }
        isFull = false;
        notifyAll();
    }

    /**
     * Sends a message through this Channel when the channel has space for it.
     *
     * If the channel is already full, this method shall wait until the
     * channel is no longer full, then send the message.
     * Otherwise, this method will immediately send the message.
     */
    synchronized void put() throws InterruptedException {
        while(isFull) {
            wait();
        }
        isFull = true;
        notifyAll();
    }

    /**
     * Attempts to retrieve a message from this Channel without waiting.
     *
     * If a message is ready for retrieval, it is retrieved and true is
     * returned.
     * Otherwise, false is returned immediately.
     *
     * @return true if the channel is ready, otherwise false.
     */
    synchronized boolean getIfReady() {
        if (isFull) {
            isFull = false;
            return true;
        } else {
            return false;
        }
    }
}
