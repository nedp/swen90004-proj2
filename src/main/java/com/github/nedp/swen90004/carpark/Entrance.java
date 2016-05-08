package com.github.nedp.swen90004.carpark;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class Entrance implements ResourceExit<Car> {

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
    public void makeAvailable() throws InterruptedException {
        sleep(Param.arrivalLapse());
        getNewCar();
        ready.put();
    }

    @Override
    public Car getNow() {
        final Car car = this.car;
        this.car = null;
        return car;
    }

    @Override
    public void reserveItem() throws InterruptedException {
        ready.get();
    }

    private void getNewCar() {
        car = Car.getNew();
        Logger.logEvent("%s arrives", car);
    }

    String state() {
        return String.format("{entrance:%6s}", car);
    }
}
