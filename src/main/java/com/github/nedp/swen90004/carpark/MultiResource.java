package com.github.nedp.swen90004.carpark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by nedp on 28/04/16.
 */
class MultiResource<T> {

    private final int nResources;
    private int level = 0;

    private final List<NullChannel> empty;
    private boolean isEmpty = true;

    private final List<Channel<T>> full;
    private final boolean[] reservations;


    MultiResource(int nResources) {
        this.nResources = nResources;
        empty = nullChannels(nResources);
        try {
            empty.get(0).put();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait", e);
        }
        full = channels(nResources);
        reservations = new boolean[nResources];
    }

    private static <S> List<Channel<S>> channels(int n) {
        final List<Channel<S>> channels = new ArrayList<>(n);
        for (int i = 0; i < n; i += 1) {
            channels.add(new Channel<>());
        }
        return Collections.unmodifiableList(channels);
    }

    private static List<NullChannel> nullChannels(int n) {
        final List<NullChannel> channels = new ArrayList<>(n);
        for (int i = 0; i < n; i += 1) {
            channels.add(new NullChannel());
        }
        return Collections.unmodifiableList(channels);
    }

    void putEmpty(int level) throws InterruptedException {
        setLevel(level);
        setEmpty();
        empty.get(level).put();
    }

    void put(int source, int destination, T item) throws InterruptedException {
        full.get(destination).put(item);
        setFull();
        assert(level == source);
        setLevel(destination);
    }

    Integer nextReservation() throws InterruptedException {
        final boolean isEmpty = empty.get(level).getNow();
        if (!isEmpty) {
            return null;
        }

        if (!reservations[level]) {
            for (int i = 0; i < nResources; i += 1) {
                if (i != level && reservations[i]) {
                    return i;
                }
            }
        }
        empty.get(level).put();
        return null;
    }

    void getEmpty(int level) throws InterruptedException {
        reserve(level);
        empty.get(level).get();
        release(level);
    }

    T get(int level) throws InterruptedException {
        return full.get(level).get();
    }

    private synchronized void reserve(int level) throws InterruptedException {
        while (reservations[level]) {
            wait();
        }
        reservations[level] = true;
    }

    private synchronized void release(int level) {
        assert(reservations[level]);
        reservations[level] = false;
        notifyAll();
    }

    private void setEmpty() {
        isEmpty = true;
    }

    private void setFull() {
        isEmpty = false;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "lift";
    }
}
