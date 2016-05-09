package com.github.nedp.swen90004.carpark;

/**
 * Repeatedly passes items from the source to the destination, subject to a
 * delay.
 *
 * Continuously moves items from the source to the destination.
 * It first waits for the source to be full and the destination to be empty.
 * Then it takes an item from the source, waits an appropriate amount of time,
 * and puts it in the destination.
 *
 * The class is generic because it can be for free, but in the swen90004 carpark
 * simulation, T is always Car.
 *
 * See the implemented interface for documentation on overridden methods.
 */
class Vehicle<T> extends Thread {
    // The id should uniquely identify a Vehicle, assist debugging.
    private final String id;

    // The Vehicle takes items from the source and gives them to the
    // destination.
    private final ResourceExit<T> source;
    private final ResourceEntry<T> destination;

    // Between taking an item and giving it away, the item is stored here.
    private T item = null;

    /**
     * Constructs a new Vehicle with a String id.
     *
     * @param id the string which identifies this vehicle.
     * @param source the ResourceExit from which this vehicle will take items.
     * @param destination the ResourceEntry into which this vehicle will place
     *                    items.
     */
    Vehicle(String id, ResourceExit<T> source, ResourceEntry<T> destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }

    /**
     * Constructs a new Vehicle with an int id.
     *
     * All parameters are as defined in
     * {@link Vehicle(String, ResourceExit, ResourceEntry)}, except:
     *
     * @param id the integer which identifies this vehicle, which will be
     *           converted to a string.
     */
    Vehicle(int id, ResourceExit<T> source, ResourceEntry<T> destination) {
        this(Integer.toString(id), source, destination);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public void run() {
        // Continuously move items from the source to the destination using
        // the following procedure:

        // * Wait for the source to be full, taking ownership of its item.
        // * Wait for the destination to be full, taking ownership of its space.
        // * Remove the item from the source, while maintaining ownership.
        // * Sleep for the time it takes to tow the item.
        // * Place the item in the destination, releasing ownership of the
        //   no-longer-empty destination..
        // * Release ownership of the source by telling it that it is available.
        while (true) {
            try {
                source.acquireWhenFull();
                destination.acquireWhenEmpty();
                item = source.getNow();
                sleep(Param.TOWING_TIME);
                destination.put(item);
                item = null;
                source.makeAvailable();
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format(
                        "Vehicle %s stopped; it was interrupted!", id), e);
            }
        }
    }

    /** Returns a human readable representation of the state of the vehicle. */
    String state() {
        return String.format("%6s", (item == null) ? "" : item);
    }
}
