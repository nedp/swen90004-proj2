package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Vehicle extends Thread {
    private final int id;
    private final Section source;
    private final Section destination;

    Vehicle(int id, Section source, Section destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Car car = this.source.get();
                this.destination.put(car);
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format(
                        "Vehicle %d stopped; it was interrupted!", id), e);
            }
        }
    }
}
