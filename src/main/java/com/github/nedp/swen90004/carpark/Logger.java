package com.github.nedp.swen90004.carpark;

/**
 * A class which logs events and state snapshots.
 *
 * Responsible for knowing whether to output just the event trace,
 * just the textual state snapshots, or both.
 */
class Logger {

    // We can log either the event trace, state snapshots, or both.
    private enum LogType {
        EVENTS, STATE, BOTH;
    }
    private static LogType LOG_TYPE = LogType.EVENTS;

    /**
     * Logs the specified event text, if event text should be logged.
     *
     * Uses string formatting of the same form as {@code System.out.printf}.
     */
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

    /**
     * Logs the specified state text, if state snapshots should be logged.
     *
     * Uses string formatting of the same form as {@code System.out.printf}.
     */
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
