package com.github.nedp.swen90004.carpark;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class Exit implements Consumer<Car> {

    private final NullChannel ready = new NullChannel();

    private Car car;

    Exit() {
        try {
            ready.put();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait!", e);
        }
    }

    @Override
    public void getEmpty() throws InterruptedException {
        ready.get();
    }

    @Override
    public void put(Car car) throws InterruptedException {
        this.car = car;
        sleep(Param.departureLapse());
        Logger.logEvent("%s departs", car);
        this.car = null;
        ready.put();
    }
}
