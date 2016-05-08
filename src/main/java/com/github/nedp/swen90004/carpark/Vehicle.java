package com.github.nedp.swen90004.carpark;

/**
 * Repeatedly passes items from the source to the destination subject to a
 * delay.
 *
 * Waits for both the source to be full and the destination to be empty
 * before the transfer is started.
 * When starting the transfer, the source's item is moved to this Vehicle.
 * The transfer then takes `Param.TOWING_TIME` milliseconds to complete.
 * Upon completion, the item is moved from this vehicle to the destination
 * and this vehicle notifies the source that it may now be considered empty.
 *
 * The class is generic because it can be for free, but in swen90004 carpark
 * simulation, T is always Car.
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

    Vehicle(String id, ResourceExit<T> source, ResourceEntry<T> destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }

    Vehicle(int id, ResourceExit<T> source, ResourceEntry<T> destination) {
        this(Integer.toString(id), source, destination);
    }

    @Override
    public String toString() {
        return id;
    }

    /**
     * Repeatedly moves items from the source to the destination using
     * the following process:
     *
     * <ol>
     *     <li>Wait for the source to be full, taking ownership of its item.
     *     </li>
     *     <li>Wait for the destination to be full, taking ownership of its
     *     space.</li>
     *     <li>Remove the item from the source, while maintaining ownership.
     *     </li>
     *     <li>Sleep for the time it takes to tow the item.</li>
     *     <li>Place the item in the destination, releasing ownership of the
     *     empty space.</li>
     *     <li>Release ownership of the source by telling it that it may be
     *     considered available.<li>
     * </ol>
     */
    @Override
    public void run() {
        while (true) {
            try {
                source.reserveItem();
                destination.reserveAvailability();
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

    String state() {
        return String.format("%6s", (item == null) ? "" : item);
    }
}
