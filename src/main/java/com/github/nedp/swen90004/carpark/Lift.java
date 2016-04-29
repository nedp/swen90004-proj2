package com.github.nedp.swen90004.carpark;

import java.util.Optional;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class Lift<T> implements Resource<T> {

    private final MultiResource<T> multiResource;
    private final int putLevel;
    private final int getLevel;

    public Lift(MultiResource<T> multiResource, int putLevel, int getLevel) {
        this.multiResource = multiResource;
        this.putLevel = putLevel;
        this.getLevel = getLevel;
    }

    @Override
    public void putEmpty() throws InterruptedException {
        multiResource.putEmpty(getLevel);
    }

    @Override
    public void put(T item) throws InterruptedException {
        if (putLevel == 0) {
            Logger.logEvent("%s enters %s to go up", item, this);
        } else {
            Logger.logEvent("%s enters %s to go down", item, this);
        }
        sleep(Param.OPERATE_TIME);
        multiResource.put(putLevel, getLevel, item);
    }

    @Override
    public void getEmpty() throws InterruptedException {
        multiResource.getEmpty(putLevel);
    }

    @Override
    public T get() throws InterruptedException {
        final T item = multiResource.get(getLevel);
        Logger.logEvent("%s exits %s", item, this);
        return item;
    }

    @Override
    public String toString() {
        return multiResource.toString();
    }

    @Override
    public String state() {
        return null;
    }
}
