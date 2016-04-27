package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Consumer extends Thread {

    private final Lift lift;

    Consumer(Lift lift) {
        this.lift = lift;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Car car = lift.getLowered();
                car.depart();
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Consumer stopped; it was interrupted!", e);
            }
        }
    }
}
