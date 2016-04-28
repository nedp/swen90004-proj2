package com.github.nedp.swen90004.carpark;

import javax.annotation.Nonnull;

/**
 * A class whose instances are Car, each with its unique Id.
 */
class Car {

    // The Id of this car
    private final int id;

    // The next ID to be allocated
    private static int currentId = 0;

    // Create a new car with a given Id
    private Car(int id) {
        this.id = id;
    }

    // Get a new car instance with a unique Id
    static Car getNew() {
        currentId += 1;
        return new Car(currentId);
    }

    // Produce the Id of this car
    private int getId() {
        return id;
    }

    // Produce an identifying string for the car
    @Override
    public String toString() {
        return String.format("[%d]", id);
    }

    void arrive() {
        System.out.printf("%s arrives\n", this);
    }

    void depart() {
        System.out.printf("%s departs\n", this);
    }
}
