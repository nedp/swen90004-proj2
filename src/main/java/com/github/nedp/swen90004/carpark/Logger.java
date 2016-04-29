package com.github.nedp.swen90004.carpark;

import java.util.Objects;

/**
 * Created by nedp on 28/04/16.
 */
class Logger {
    private enum LogType {
        EVENT, STATE, BOTH;
    }

    private static LogType LOG_TYPE = LogType.BOTH;

    static void logEvent(String format, Object... arguments) {
        switch (LOG_TYPE) {
            case EVENT:
            case BOTH:
                System.out.printf(format + "\n", arguments);
                break;

            default: // Do nothing
                break;
        }
    }
}
