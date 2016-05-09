package com.github.nedp.swen90004.carpark;

/**
 * An adapter for the Lift class satisfying the Resource interface, allowing
 * the lift to be used by regular vehicles, so no special vehicle classes are
 * needed.
 *
 * In the assignment specification there are always two paths through the lift,
 * top to bottom and bottom to top.
 * However this class would allow additional levels (and thus paths through
 * the lift) to be added with minimal effort.
 *
 * See the implemented interface for documentation on overridden methods.
 */
class LiftResource implements Resource<Car> {

    // The Lift via which this LiftResource goes.
    private final Lift<Car> lift;

    // The level which the lift must be at to put a car into this LiftResource.
    private final int putIndex;

    // The level which the lift must be at to get a car from this liftPath.
    private final int getIndex;

    // A human readable description of the reason things enter the lift
    // through this object, based on direction of travel.
    private final String direction;
    private static final String UP = "to go up";
    private static final String DOWN = "to go down";
    private static final String SAME = "to pass through";

    /**
     * Constructs a new LiftResource without performing any actions.
     *
     * @param lift the Lift by which this LiftResource is backed.
     * @param putIndex the index of the lift at which Cars should put into the
     *                 lift.
     * @param getIndex the index of the lift at which Cars should be gotten
     *                 from the lift.
     */
    LiftResource(Lift<Car> lift, int putIndex, int getIndex) {
        this.lift = lift;
        this.putIndex = putIndex;
        this.getIndex = getIndex;
        if (putIndex < getIndex) {
            direction = UP;
        } else if (putIndex > getIndex) {
            direction = DOWN;
        } else {
            direction = SAME;
        }
    }

    @Override
    public void makeAvailable() throws InterruptedException {
        lift.makeAvailable(getIndex);
    }

    @Override
    public void acquireWhenFull() throws InterruptedException {
        this.lift.acquireWhenFull(getIndex);
    }

    @Override
    public void put(Car item) throws InterruptedException {
        Logger.logEvent("%s enters %s %s", item, lift, direction);
        lift.put(getIndex, item);
    }

    @Override
    public void acquireWhenEmpty() throws InterruptedException {
        lift.acquireWhenEmpty(putIndex);
    }

    @Override
    public Car getNow() {
        final Car item = this.lift.getNow();
        Logger.logEvent("%s exits %s", item, lift);
        return item;
    }
}
