package com.github.nedp.swen90004.carpark;

import java.util.Optional;

/**
 * Created by nedp on 28/04/16.
 */
class Channel<T> {

    private boolean isFull = false;

    private T item = null;

    synchronized T get() throws InterruptedException {
        while (!isFull) {
            wait();
        }
        isFull = false;
        notifyAll();
        return item;
    }

    synchronized void put(T item) throws InterruptedException {
        while(isFull) {
            wait();
        }
        this.item = item;
        isFull = true;
        notifyAll();
    }

    synchronized T getNow() {
        if (isFull) {
            isFull = false;
            return item;
        } else {
            return null;
        }
    }

    synchronized Optional<T> getIfReady() {
        if (isFull) {
            isFull = false;
            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }
}
