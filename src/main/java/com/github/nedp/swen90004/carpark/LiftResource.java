package com.github.nedp.swen90004.carpark;

/**
 * A resource backed by a lift, representing a single path through the lift.
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
    }

    @Override
    public void makeAvailable() throws InterruptedException {
        lift.makeAvailable(getIndex);
    }

    @Override
    public void reserveItem() throws InterruptedException {
        this.lift.waitForFull(getIndex);
    }

    @Override
    public void put(Car item) throws InterruptedException {
        if (putIndex < getIndex) {
            Logger.logEvent("%s enters %s to go up", item, lift);
        } else if (putIndex > getIndex) {
            Logger.logEvent("%s enters %s to go down", item, lift);
        } else {
            Logger.logEvent("%s enters %s to pass through", item, lift);
        }
        lift.put(putIndex, getIndex, item);
    }

    @Override
    public void reserveAvailability() throws InterruptedException {
        lift.getEmpty(putIndex);
    }

    @Override
    public Car getNow() {
        final Car item = this.lift.getNow(getIndex);
        Logger.logEvent("%s exits %s", item, this);
        return item;
    }
}
