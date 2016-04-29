package com.github.nedp.swen90004.carpark;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class LiftResource<T> implements Resource<T> {

    private final Lift<T> lift;
    private final int putLevel;
    private final int getLevel;

    T item = null;

    public LiftResource(Lift<T> lift, int putLevel, int getLevel) {
        this.lift = lift;
        this.putLevel = putLevel;
        this.getLevel = getLevel;
    }

    @Override
    public void putEmpty() throws InterruptedException {
        lift.putEmpty(getLevel);
    }

    @Override
    public void waitForFull() throws InterruptedException {
        item = this.lift.get(getLevel);
    }

    @Override
    public void put(T item) throws InterruptedException {
        if (putLevel == 0) {
            Logger.logEvent("%s enters %s to go up", item, this);
        } else {
            Logger.logEvent("%s enters %s to go down", item, this);
        }
        lift.put(putLevel, getLevel, item);
    }

    @Override
    public void getEmpty() throws InterruptedException {
        lift.getEmpty(putLevel);
    }

    @Override
    public T getNow() {
        final T item = this.item;
        this.item = null;
        Logger.logEvent("%s exits %s", item, this);
        return item;
    }

    @Override
    public String toString() {
        return lift.toString();
    }
}
