package com.github.nedp.swen90004.carpark;

import static com.github.nedp.swen90004.carpark.Level.LOWERED;
import static com.github.nedp.swen90004.carpark.Level.RAISED;
import static java.lang.Thread.sleep;

/**
 * Created by nedp on 27/04/16.
 */
class Lift {

    private Car car;
    private Level level;
    private boolean reservedLowered;
    private boolean reservedRaised;

    Lift() {
        car = null;
        level = LOWERED;
        reservedLowered = false;
        reservedRaised = false;
    }

    synchronized void operateIfNeeded() throws InterruptedException {
        if (car != null) {
            return;
        }
        switch (level) {
            case LOWERED:
                if (reservedRaised && !reservedLowered) lowerNow();
            case RAISED:
                if (reservedLowered && !reservedRaised) raiseNow();
        }
        notifyAll();
    }

    private void raiseNow() throws InterruptedException {
        System.out.println("lift starts raising");
        sleep(Param.OPERATE_TIME);
        System.out.println("lift finishes raising");
        level = RAISED;
    }

    private void lowerNow() throws InterruptedException {
        System.out.println("lift starts lowering");
        sleep(Param.OPERATE_TIME);
        System.out.println("lift finishes lowering");
        level = LOWERED;
    }

    synchronized void putAndRaise(Car car) throws InterruptedException {
        while (level != LOWERED || this.car != null) {
            reservedLowered = true;
            wait();
        }
        this.car = car;
        System.out.printf("%s enters the lift to go up\n", car);
        reservedLowered = false;
        raiseNow();
        notifyAll();
    }

    synchronized void putAndLower(Car car) throws InterruptedException {
        while (level != RAISED || this.car != null) {
            reservedRaised = true;
            wait();
        }
        this.car = car;
        System.out.printf("%s enters the lift to go down\n", car);
        reservedLowered = false;
        lowerNow();
        notifyAll();
    }

    synchronized Car getRaised() throws InterruptedException {
        while (level != RAISED || this.car == null) {
            wait();
        }
        final Car car = this.car;
        this.car = null;
        notifyAll();
        return car;
    }

    synchronized Car getLowered() throws InterruptedException {
        while (level != LOWERED || this.car == null) {
            wait();
        }
        final Car car = this.car;
        this.car = null;
        notifyAll();
        return car;
    }
}
