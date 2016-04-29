package com.github.nedp.swen90004.carpark;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class Exit<T> implements Consumer<T> {

    private final NullChannel ready = new NullChannel();

    private T item;

    Exit() {
        try {
            ready.put();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait!", e);
        }
    }

    @Override
    public void getEmpty() throws InterruptedException {
        ready.get();
        sleep(Param.departureLapse());
        Logger.logEvent("%s departs", item);
        this.item = null;
    }

    @Override
    public void put(T item) throws InterruptedException {
        ready.put();
        this.item = item;
    }

    String state() {
        return String.format("{exit:%6s}", item);
    }
}
