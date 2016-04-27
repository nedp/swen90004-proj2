package com.github.nedp.swen90004.carpark;

import java.util.Random;

/**
 * Parameters that influence the behaviour of the system.
 */
class Param {

    // The number of car park spaces
    final static int SECTIONS = 6;

    // The time interval at which Main checks threads are alive
    final static int MAIN_INTERVAL = 50;

    // The time it takes to operate the lift
    final static int OPERATE_TIME = 800;

    // The time it takes to tow
    final static int TOWING_TIME = 1200;

    // The maximum amount of time between car arrivals
    private final static int MAX_ARRIVE_INTERVAL = 2400;

    // The maximum amount of time between car departures
    private final static int MAX_DEPART_INTERVAL = 800;

    // The maximum amount of time between operating the lift
    private final static int MAX_OPERATE_INTERVAL = 6400;

    /**
     * For simplicity, we assume uniformly distributed time lapses.
     * An exponential distribution might be a fairer assumption.
     */
    private static int arrivalLapse() {
        Random random = new Random();
        return random.nextInt(MAX_ARRIVE_INTERVAL);
    }

    private static int departureLapse() {
        Random random = new Random();
        return random.nextInt(MAX_DEPART_INTERVAL);
    }

    static int operateLapse() {
        Random random = new Random();
        return random.nextInt(MAX_OPERATE_INTERVAL);
    }
}

