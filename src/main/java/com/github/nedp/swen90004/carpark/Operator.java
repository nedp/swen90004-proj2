package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Operator extends Thread {

    private final Lift lift;

    Operator(Lift lift) {
        this.lift = lift;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(Param.operateLapse());
                lift.operateIfNeeded();
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Operator stopped; it was interrupted!", e);
            }
        }
    }
}
