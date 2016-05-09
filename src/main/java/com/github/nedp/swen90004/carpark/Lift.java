package com.github.nedp.swen90004.carpark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;

/**
 * A single resource-like object with an arbitrary but constant number of
 * exits and entrances.
 *
 * Responsible for:
 * <ul>
 *     <li>providing a set of entry/exit point pairs, one per level</li>
 *     <li>ensuring that only one set of entry/exit point pairs is available
 *     at a given time, since the lift can only be on one level at a time</li>
 *     <li>synchronising access to the entry/exit points to make sure that
 *     only one point is accessed at a time</li>
 * </ul>
 *
 * The class is generic because it can be for free, but in the swen90004 carpark
 * simulation, T is always Car.
 *
 * See the implemented interface for documentation on overridden methods.
 */
class Lift<T> {

    // The lift has a number of levels equal to nLevels,
    // but at any given time is only available at the current level.
    private final int nLevels;
    private int currentLevel = 0;

    // A human readable identifier for the lift.
    private final String name;

    // Used for tracking the 'state' of the lift; since channels don't allow
    // observation of their contents without retrieving it, this variable is
    // required to allow the printing of state.
    // Items are stored in this variable when they are put into the lift,
    // and removed from this variable when they are gotten from the lift.
    private T item = null;

    // When the lift is unavailable, all channels have no messages in them.
    // When the lift is available at level X, then:
    // * if the lift has no item, `empty[X]` has a message in it.
    // * if the lift has an item, `full[X]` has a message in it.
    private final List<Channel<Object>> empty;
    private final List<Channel<T>> full;

    // When the lift is being waited for on level X, reservations[X] is true,
    // otherwise it is false.
    // This allows the lift to tell the operator whether the it needs to move,
    // and if so, where it needs to move to,
    private final boolean[] reservations;

    /** Constructs a lift with the specified name and number of levels. */
    Lift(int nLevels, String name) {
        this.nLevels = nLevels;
        this.name = name;

        // Make the lift empty and available at level 0.
        // It should be impossible for this call to wait, because it is
        // a fresh channel.
        empty = channels(nLevels);
        full = channels(nLevels);
        try {
            empty.get(0).put(new Object());
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait", e);
        }

        // Make the lift unreserved; boolean arrays default to all false.
        reservations = new boolean[nLevels];
    }

    // Returns an unmodifiable list of n channels with the specified content
    // type.
    private static <S> List<Channel<S>> channels(int n) {
        final List<Channel<S>> channels = new ArrayList<>(n);
        for (int i = 0; i < n; i += 1) {
            channels.add(new Channel<>());
        }
        return Collections.unmodifiableList(channels);
    }

    /**
     * Waits Param.OPERATE_TIME milliseconds then makes the lift available at
     * the specified level.
     *
     * If the lift is already at the specified level, then the
     * Param.OPERATE_TIME wait does not occur.
     *
     * If the lift is already available at the specified level, waits until it
     * is unavailable at that level.
     * If ownership is respected, this wait should never occur.
     *
     * The caller is responsible for ensuring that the lift has been made empty
     * and that it has ownership of the lift.
     *
     * @param level the level at which to make the lift available.
     */
    void makeAvailable(int level) throws InterruptedException {
        setCurrentLevel(level);
        empty.get(level).put(new Object());
    }

    /**
     * Puts the specified item in the lift at the specified destination level.
     *
     * Waits for the lift to not be full at the specified level;
     * if ownership is respected, this method should never wait.
     *
     * Does not move the lift or wait for Param.OPERATE_TIME; this occurs
     * when a caller takes ownership of the lift at that level instead.
     *
     * @param level the level at which to put the item
     * @param item the item to put at that level
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    void put(int level, T item) throws InterruptedException {
        this.item = item;
        full.get(level).put(item);
    }

    /**
     * Takes ownership of the lift and returns a level on which it is reserved,
     * but only if the lift is currently empty, and it is reserved exclusively
     * on a different level to its current level.
     *
     * If Optional.of(..) is returned, then the caller takes ownership of the
     * lift's emptiness.
     * Otherwise, the caller does not take ownership of the lift's emptiness.
     *
     * @return Optional.empty() if the lift is reserved on its current level
     * or not at all; otherwise Optional.of(level) where level is the level at
     * which the lift is reserved.
     */
    Optional<Integer> nextReservation() {
        // Check if the lift is empty and take ownership of its emptiness if so.
        final Optional<Object> emptyMessage =
                empty.get(currentLevel).getIfReady();

        // If there was no message, then we haven't taken ownership of
        // the lift's emptiness, so we don't need to relinquish it.
        if (!emptyMessage.isPresent()) return Optional.empty();

        // If the lift is not reserved on its current level and it is reserved
        // on a different level, then return that other level, keeping ownership
        // of the lift's emptiness.
        if (!reservations[currentLevel]) {
            for (int i = 0; i < nLevels; i += 1) {
                if (reservations[i]) {
                    return Optional.of(i);
                }
            }
        }

        // There is no reservation on a different level, so there is no need
        // for the caller to receive ownership of the lift's emptiness;
        // relinquish the ownership.
        // This call should never wait if ownership is respected, so
        // an interruption implies a programming error.
        try {
            empty.get(currentLevel).put(new Object());
        } catch (InterruptedException e) {
            throw new RuntimeException(
                    "waited because ownership not respected", e);
        }
        return Optional.empty();
    }

    /**
     * Waits until the lift is available, empty, and at the specified level,
     * then takes ownership of it.
     *
     * The lift guarantees that only one caller may take ownership of it at a
     * time.
     *
     * @param level the level at which the lift must be.
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the lift to become available at the specified level.
     */
    void acquireWhenEmpty(int level) throws InterruptedException {
        reserve(level);
        empty.get(level).get();
        release(level);
    }

    /**
     * Retrieves the item from the lift, assuming it is owned by the caller.
     *
     * The caller must guarantee that they have ownership of the lift
     * and that they have not already retrieved the item.
     *
     * The lift is made empty by this method, but ownership is not relinquished.
     *
     * @return the item in the lift.
     */
    T getNow() {
        final T item = this.item;
        this.item = null;
        return item;
    }

    /**
     * Waits until an item is present in the lift and going to the specified
     * level, waits Param.OPERATE_TIME milliseconds, then takes ownership of the
     * lift and sets its level to the specified level without retrieving the
     * item.
     *
     * If the lift is already at the specified level, then the
     * Param.OPERATE_TIME wait does not occur.
     *
     * The lift guarantees that only one caller may take ownership at a time.
     *
     * The method waits for Param.OPERATE_TIME milliseconds, then sets its level
     * to the specified level.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     * for the required conditions or while setting the lift's level.
     */
    void acquireWhenFull(int level) throws InterruptedException {
        full.get(level).get();
        setCurrentLevel(level);
    }

    // Waits until the lift is not reserved at the specified level, then
    // reserves it at that level.
    private synchronized void reserve(int level) throws InterruptedException {
        while (reservations[level]) {
            wait();
        }
        reservations[level] = true;
    }

    // Releases the reservation of the lift at the specified level.
    private synchronized void release(int level) {
        assert(reservations[level]);
        reservations[level] = false;
        notifyAll();
    }

    // Sets the current level of the lift, waiting Param.OPERATE_TIME for the
    // lift's level to change.
    private void setCurrentLevel(int currentLevel) throws InterruptedException {
        if (currentLevel != this.currentLevel) {
            sleep(Param.OPERATE_TIME);
        }
        this.currentLevel = currentLevel;
    }

    /**
     * A human readable representation of the lift's state at a specific level.
     *
     * @param level the level of interest
     * @return an empty string if the lift is not at the specified level,
     * otherwise a representation of the lift's state.
     */
    String state(int level) {
        return (this.currentLevel != level) ? ""
                : String.format("{%8s:%6s}", this, item);
    }

    @Override
    public String toString() {
        return name;
    }
}
