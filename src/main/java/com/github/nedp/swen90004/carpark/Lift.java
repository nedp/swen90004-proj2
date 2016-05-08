package com.github.nedp.swen90004.carpark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class Lift<T> {

    private final int nResources;
    private final String name;
    private int index = 0;

    private final List<NullChannel> empty;

    private T item = null;

    private final List<Channel<T>> full;
    private final boolean[] reservations;

    Lift(int nResources, String name) {
        this.nResources = nResources;
        this.name = name;
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

    void putEmpty(int index) throws InterruptedException {
        setIndex(index);
        empty.get(index).put();
    }

    void put(int source, int destination, T item) throws InterruptedException {
        assert(index == source);
        this.item = item;
        full.get(destination).put(item);
    }

    Integer nextReservation() throws InterruptedException {
        final boolean isEmpty = empty.get(index).getIfReady();
        if (!isEmpty) {
            return null;
        }

        if (!reservations[index]) {
            for (int i = 0; i < nResources; i += 1) {
                if (reservations[i]) {
                    return i;
                }
            }
        }
        empty.get(index).put();
        return null;
    }

    void getEmpty(int index) throws InterruptedException {
        reserve(index);
        empty.get(index).get();
        release(index);
    }

    T getNow(int index) {
        assert(this.index == index);
        final T item = this.item;
        this.item = null;
        return item;
    }

    void waitForFull(int index) throws InterruptedException {
        full.get(index).get();
        setIndex(index);
    }

    private synchronized void reserve(int index) throws InterruptedException {
        while (reservations[index]) {
            wait();
        }
        reservations[index] = true;
    }

    private synchronized void release(int index) {
        assert(reservations[index]);
        reservations[index] = false;
        notifyAll();
    }

    private void setIndex(int index) throws InterruptedException {
        if (index != this.index) {
            sleep(Param.OPERATE_TIME);
        }
        this.index = index;
    }

    String state(int index) {
        return (this.index != index) ? ""
                : String.format("{%8s:%6s}", this, item);
    }

    @Override
    public String toString() {
        return name;
    }
}
