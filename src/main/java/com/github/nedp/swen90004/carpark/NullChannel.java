package com.github.nedp.swen90004.carpark;

import java.util.Optional;

/**
 * An abstraction to allow simple synchronized message passing
 * between threads without message content.
 *
 * A NullChannel doesn't hold items, but does conceptually hold
 * empty messages.
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

    // true if this Channel currently has an item, otherwise false.
    private boolean isFull = false;

    // The item most recently sent through this channel.
    synchronized void get() throws InterruptedException {
        while (!isFull) {
            wait();
        }
        isFull = false;
        notifyAll();
    }

    synchronized void put() throws InterruptedException {
        while(isFull) {
            wait();
        }
        isFull = true;
        notifyAll();
    }

    synchronized boolean getIfReady() {
        if (isFull) {
            isFull = false;
            return true;
        } else {
            return false;
        }
    }
}
