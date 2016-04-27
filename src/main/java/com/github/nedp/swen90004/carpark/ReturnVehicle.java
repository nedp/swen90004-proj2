package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class ReturnVehicle extends Thread {
    private final Section source;
    private final Lift destination;

    ReturnVehicle(Section source, Lift destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Car car = this.source.get();
                this.destination.putAndLower(car);
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Launch Vehicle stopped; it was interrupted!", e);
            }
        }
    }
}
