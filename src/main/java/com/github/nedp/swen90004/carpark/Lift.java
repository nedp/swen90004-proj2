package com.github.nedp.swen90004.carpark;

import java.util.Optional;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
public class Lift<T> implements Resource<T> {

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
        sleep(Param.OPERATE_TIME);
        multiResource.put(putLevel, getLevel, item);
    }

    @Override
    public String putMessage() {
        if (putLevel == 0) {
            return String.format("enters %s to go up", this);
        } else {
            return String.format("enters %s to go down", this);
        }
    }

    @Override
    public void getEmpty() throws InterruptedException {
        multiResource.getEmpty(putLevel);
    }

    @Override
    public T get() throws InterruptedException {
        return multiResource.get(getLevel);
    }

    @Override
    public Optional<String> getMessage() {
        return Optional.of(String.format("leaves %s", this));
    }

    @Override
    public String toString() {
        return multiResource.toString();
    }
}
