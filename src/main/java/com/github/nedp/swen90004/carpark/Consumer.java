package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 28/04/16.
 */
interface Consumer<T> {
    void put(T item) throws InterruptedException;

    void getEmpty() throws InterruptedException;
}
