package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Producer extends Thread {

    private final Lift lift;

    Producer(Lift lift) {
        this.lift = lift;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Car car = Car.getNew();
                car.arrive();
                lift.putAndRaise(car);
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Producer stopped; it was interrupted!", e);
            }
        }
    }
}
