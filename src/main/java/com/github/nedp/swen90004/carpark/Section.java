package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Section {

    private final int id;

    private Car car = null;

    Section(int id) {
        this.id = id;
    }

    synchronized void put(Car car) throws InterruptedException {
        while (this.car != null) {
            wait();
        }
        this.car = car;
        System.out.printf("%s enters section %d\n", car, id  );
        notifyAll();
    }

    synchronized Car get() throws InterruptedException {
        while (this.car == null) {
            wait();
        }
        Car car = this.car;
        this.car = null;
        notifyAll();
        return car;
    }
}
