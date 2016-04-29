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
                operateIfNeeded();
            } catch (InterruptedException e) {
                throw new RuntimeException(
                        "Operator stopped; it was interrupted!", e);
            }
        }
    }

    private void operateIfNeeded() throws InterruptedException {
        final Integer destination = lift.nextReservation();
        if (null == destination) {
            return;
        }
        if (destination == 0) {
            Logger.logEvent("%s goes down", lift);
        } else {
            Logger.logEvent("%s goes up", lift);
        }
        sleep(Param.OPERATE_TIME);
        Logger.logEvent("%s arrives", lift);
        lift.putEmpty(destination);
    }
}
