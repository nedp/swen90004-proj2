package com.github.nedp.swen90004.carpark;

import java.util.Optional;

/**
 * Created by nedp on 28/04/16.
 */
class NullChannel {

    private boolean isFull = false;

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

    synchronized boolean getNow() {
        if (isFull) {
            isFull = false;
            return true;
        } else {
            return false;
        }
    }
}
