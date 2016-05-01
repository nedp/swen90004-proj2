package com.github.nedp.swen90004.carpark;

import java.util.Objects;

/**
 * Created by nedp on 28/04/16.
 */
class Logger {

    private enum LogType {
        EVENTS, STATE, BOTH;
    }

    private static LogType LOG_TYPE = LogType.EVENTS;

    static void logEvent(String format, Object... arguments) {
        switch (LOG_TYPE) {
            case EVENTS:
            case BOTH:
                System.out.printf(format + "\n", arguments);
                break;

            default: // Do nothing
                break;
        }
    }

    static void logState(String format, Object... arguments) {
        switch (LOG_TYPE) {
            case STATE:
            case BOTH:
                System.out.printf(format, arguments);
                break;

            default: // Do nothing
                break;
        }
    }

}
