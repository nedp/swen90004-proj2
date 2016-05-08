package com.github.nedp.swen90004.carpark;

/**
 * A class whose instances are cars, each with its unique ID.
 *
 * See implemented interfaces and superclasses for documentation on overridden
 * methods.
 */
class Car {

    // The ID of this car.
    private final int id;

    // The ID to be allocated to the next car.
    private static int nextId = 0;

    // Create a new car with a given ID.
    private Car(int id) {
        this.id = id;
    }

    // Get a new car instance with a unique ID.
    static Car getNew() {
        final Car car = new Car(nextId);
        nextId += 1;
        return car;
    }

    @Override
    public String toString() {
        return String.format("[%d]", id);
    }
}
