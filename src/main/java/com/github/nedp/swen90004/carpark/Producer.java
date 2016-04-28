package com.github.nedp.swen90004.carpark;

import java.util.Optional;

/**
 * Created by nedp on 28/04/16.
 */
interface Producer<T> {
    void putEmpty() throws InterruptedException;

    T get() throws InterruptedException;

    Optional<String> getMessage();
}
