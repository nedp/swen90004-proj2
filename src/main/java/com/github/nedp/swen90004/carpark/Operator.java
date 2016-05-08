package com.github.nedp.swen90004.carpark;

/**
 * A thread which simulates the Operator of the lift.
 *
 * The Operator continuously:
 * <ol>
 *     <li>Waits a randomised amount of time according to Param.operatorLapse().
 *     </li>
 *     <li>Checks if the lift needs to be operated and operates it if so.</li>
 * </ol>
 *
 * The lift "needs to be operated" when it is reserved level Y, where Y =/= X,
 * and it is not reserved on level X.
 * In this circumstance the operator will move the lift to level Y.
 *
 * See the implemented interface for documentation on overridden methods.
 */
class Operator extends Thread {

    // The lift which this Operator operates.
    private final Lift lift;

    /** Constructs a new operator for the specified lift. */
    Operator(Lift lift) {
        this.lift = lift;
    }

    @Override
    public void run() {
        // Repeatedly wait and operate the lift if needed.
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

        // Report that the lift is moving using the Logger.
        // These messages should be changed if more levels are added.
        if (destination == 0) {
            Logger.logEvent("%s goes down", lift);
        } else {
            Logger.logEvent("%s goes up", lift);
        }

        // Move the lift, signal its availability, and log its arrival.
        sleep(Param.OPERATE_TIME);
        lift.makeAvailable(destination);
        Logger.logEvent("%s arrives", lift);
    }
}
