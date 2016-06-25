package com.github.nedp.swen90004.carpark;

/**
 * An implementation of the Resource interface for Car cars,
 * with support for observing the state of the section as a string.
 *
 * This implementation allows Cars to be placed into and retrived from it,
 * with no extra restrictions other than those specified for the Resource
 * interface (and its super-interfaces).
 *
 * See the implemented interface for documentation on overridden methods.
 */
class Section implements Resource<Car>, Freeable {

    // Should uniquely identify the section, assisting with state and event
    // messages.
    private final int id;

    // A channel for (internal) signaling of the availability of the section
    // for receiving new cars.
    private final Channel<Object> empty = new Channel<>();

    // A channel for synchronising the getting and putting of Cars.
    private final Channel<Car> full = new Channel<>();

    // A channel for (internal) signaling of whether the section is considered
    // 'free' for the purposes of the Freeable interface.
    private final Channel<Object> free = new Channel<>();

    // A message characterising the act of getting something from the resource.
    private final String getMessage;

    // A message characterising the act of putting something into the resource.
    private final String putMessage;

    // Used for tracking the 'state' of the section; since channels don't allow
    // observation of their contents without retrieving it, this variable is
    // required to allow the printing of state.
    // Cars are stored in this variable when they are put into the section,
    // and removed from this variable when they are gotten from the section.
    private Car car = null;

    // Constructs the section with the specified id and messages for
    // getting/putting cars.
    private Section(int id, String getMessage, String putMessage) {
        this.id = id;
        this.getMessage = getMessage;
        this.putMessage = putMessage;

        // Signal that the section is initially both available and free.
        // It should be impossible for this call to wait, because it is
        // a fresh channel.
        try {
            empty.put(new Object());
            free.put(new Object());
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait!", e);
        }
    }

    /**
     * Constructs the section with default 'leaves/enters section X' messages,
     * where X is the specified id.
     *
     * @param id the id of the section.
     */
    Section(int id) {
        this(id, String.format("leaves section %d", id),
                String.format("enters section %d", id));
    }

    @Override
    public void makeAvailable() throws InterruptedException {
        empty.put(new Object());

        // A Section can be considered free after it has been made available.
        free.put(new Object());
    }

    @Override
    public Car getNow() {
        final Car car = this.car;
        this.car = null;
        Logger.logEvent("%s %s", car, getMessage);
        return car;
    }

    @Override
    public void acquireWhenFull() throws InterruptedException {
        car = this.full.get();
    }

    @Override
    public void put(Car car) throws InterruptedException {
        full.put(car);
        this.car = car;
        Logger.logEvent("%s %s", car, putMessage);
    }

    @Override
    public void acquireWhenEmpty() throws InterruptedException {
        empty.get();

        // When a section is acquired while empty, it can no longer be
        // considered free until it is made available again.
        free.get();
    }

    /** Returns a human readable representation of this section's state. */
    String state() {
        return String.format("{%4d:%6s}", id, car);
    }

    @Override
    public void waitUntilFree() throws InterruptedException {
        // We don't want to acquire any sort of lock with this method,
        // we just want to make sure we wait until the section is in fact free.
        free.put(free.get());
    }
}
