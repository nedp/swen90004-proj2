package com.github.nedp.swen90004.carpark;

import java.util.Random;

/**
 * Parameters that influence the behaviour of the system.
 */
class Param {

    // The number of car park spaces
    final static int SECTIONS = 6;

    // The time interval at which Main checks threads are alive
    final static int MAIN_INTERVAL = 60;

    // The time it takes to operate the lift
    final static int OPERATE_TIME = 120;

    // The time it takes to tow
    final static int TOWING_TIME = 680;

    // The maximum amount of time between car arrivals
    private final static int MAX_ARRIVE_INTERVAL = 960;

    // The maximum amount of time between car departures
    private final static int MAX_DEPART_INTERVAL = 240;

    // The maximum amount of time between operating the lift
    private final static int MAX_OPERATE_INTERVAL = 320;

    /**
     * For simplicity, we assume uniformly distributed time lapses.
     * An exponential distribution might be a fairer assumption.
     */
    static int arrivalLapse() {
        Random random = new Random();
        return random.nextInt(MAX_ARRIVE_INTERVAL);
    }

    static int departureLapse() {
        Random random = new Random();
        return random.nextInt(MAX_DEPART_INTERVAL);
    }

    static int operateLapse() {
        Random random = new Random();
        return random.nextInt(MAX_OPERATE_INTERVAL);
    }
}

