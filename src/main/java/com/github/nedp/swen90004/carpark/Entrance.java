package com.github.nedp.swen90004.carpark;

import java.util.Optional;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class Entrance implements Producer<Car> {

    private final NullChannel ready = new NullChannel();
    private Car car;

    Entrance() {
        getNewCar();
        try {
            ready.put();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait!", e);
        }
    }

    @Override
    public void putEmpty() throws InterruptedException {
        car = null;
        sleep(Param.arrivalLapse());
        getNewCar();
        ready.put();
    }

    @Override
    public Car getNow() {
        return car;
    }

    @Override
    public void waitForFull() throws InterruptedException {
        ready.get();
    }

    private void getNewCar() {
        car = Car.getNew();
        Logger.logEvent("%s arrives", car);
    }
}
