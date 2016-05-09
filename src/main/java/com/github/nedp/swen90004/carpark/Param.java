package com.github.nedp.swen90004.carpark;

import java.util.Random;

/**
 * Parameters that influence the behaviour of the system.
 */
class Param {

    // Use a single static random instance to avoid unnecessary work.
    private final static Random random = new Random();

    // The number of car park spaces
    final static int SECTIONS = 6;

    // The time interval at which Main checks threads are alive
    final static int MAIN_INTERVAL = 60;

    // The time it takes to operate the lift
    final static int OPERATE_TIME = 480;

    // The time it takes to tow
    final static int TOWING_TIME = 660;

    // The maximum amount of time between car arrivals
    private final static int MAX_ARRIVE_INTERVAL = 960;

    // The maximum amount of time between car departures
    private final static int MAX_DEPART_INTERVAL = 1680;

    // The maximum amount of time between operating the lift
    private final static int MAX_OPERATE_INTERVAL = 2400;

    /**
     * Randomly selects a lapse from a roughly exponential distribution.
     * The output is limited to a maximum of MAX_ARRIVE_INTERVAL.
     */
    static int arrivalLapse() {
        return cappedExponentialDistribution(MAX_ARRIVE_INTERVAL);
    }

    /**
     * Randomly selects a lapse from a roughly exponential distribution.
     * The output is limited to a maximum of MAX_DEPART_INTERVAL.
     */
    static int departureLapse() {
        return cappedExponentialDistribution(MAX_DEPART_INTERVAL);
    }

    /**
     * Randomly selects a lapse from a roughly exponential distribution.
     * The output is limited to a maximum of MAX_OPERATE_INTERVAL.
     */
    static int operateLapse() {
        return cappedExponentialDistribution(MAX_OPERATE_INTERVAL);
    }

    // Returns a random number from a roughly exponential distribution
    // with the specified maximum, with an expected value of half the max.
    private static int cappedExponentialDistribution(int max) {
        // For an exponential distribution, the expected value is 1 / lambda.
        // We want an expected value of half the maximum to match the normal
        // distribution, so (1 / lambda = max / 2) => (lambda = 2 / max).
        final double lambda = 2.0 / (double)max;

        // Choose a preliminary result from the exponential distribution.
        final double rng = random.nextDouble();
        final int result = (int) Math.floor((Math.log(1.0 - rng) / -lambda));

        // Cap the result to the maximum.
        return result > max ? max : result;
    }
}

