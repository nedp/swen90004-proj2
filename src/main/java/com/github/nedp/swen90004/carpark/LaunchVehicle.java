package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class LaunchVehicle extends Thread {
    private final Lift source;
    private final Section destination;

    LaunchVehicle(Lift source, Section destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Car car = this.source.getRaised();
                this.destination.put(car);
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Launch Vehicle stopped; it was interrupted!", e);
            }
        }
    }
}
