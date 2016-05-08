package com.github.nedp.swen90004.carpark;

import static java.lang.Thread.sleep;

/**
 * An implementation of ResourceEntry but not ResourceExit for Cars,
 * which acts as the exit of the carpark.
 *
 * Cars may enter this Exit like a normal resource, but never exit it.
 * Instead, cars which enter this exit are immediately destroyed.
 * When reserving the availability of this exit, a random amount of time is
 * waited, determined by Param.departureLapse().
 *
 * This Exit has no state, since Cars are considered to have left the car park
 * as soon as they are towed from the lift.
 *
 * See the implemented interface for documentation on overridden methods.
 */
class Exit<T> implements ResourceEntry<T> {

    @Override
    public void reserveAvailability() throws InterruptedException {
        sleep(Param.departureLapse());
    }

    @Override
    public void put(T item) throws InterruptedException { }
}
