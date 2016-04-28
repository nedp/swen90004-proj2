package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Operator extends Thread {

    private final MultiResource multiResource;

    Operator(MultiResource multiResource) {
        this.multiResource = multiResource;
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
        final Integer destination = multiResource.nextReservation();
        if (null == destination) {
            return;
        }
        if (destination == 0) {
            Logger.logEvent("%s goes down\n", multiResource);
        } else {
            Logger.logEvent("%s goes up\n", multiResource);
        }
        sleep(Param.OPERATE_TIME);
        Logger.logEvent("%s arrives\n", multiResource);
        multiResource.putEmpty(destination);
    }
}
