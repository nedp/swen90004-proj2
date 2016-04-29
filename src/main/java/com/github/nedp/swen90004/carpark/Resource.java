package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 28/04/16.
 */
interface Resource<T> extends Consumer<T>, Producer<T> {
    String state();
}
