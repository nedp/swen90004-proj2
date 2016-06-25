package com.github.nedp.swen90004.carpark;

/**
 * A lift resource which is guarded by making it wait until
 * a Freeable object is free before allowing the
 * GuardedLiftResource to be acquired in an empty state.
 *
 * See overriden methods for full documentation.
 */
public class GuardedLiftResource extends LiftResource {

    final Freeable freeable;

    /**
     * Constructs a new GuardedLiftResource without performing any actions.
     *
     * @param lift     the Lift by which this LiftResource is backed.
     * @param putIndex the index of the lift at which Cars should put into the
     *                 lift.
     * @param getIndex the index of the lift at which Cars should be gotten
     *
     */
    GuardedLiftResource(Lift<Car> lift,
                        int putIndex,
                        int getIndex,
                        Freeable freeable) {
        super(lift, putIndex, getIndex);
        this.freeable = freeable;
    }

    @Override
    public void acquireWhenEmpty() throws InterruptedException {
        freeable.waitUntilFree();
        super.acquireWhenEmpty();
    }
}
