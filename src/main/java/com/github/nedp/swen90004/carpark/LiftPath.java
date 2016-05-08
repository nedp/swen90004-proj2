package com.github.nedp.swen90004.carpark;

import static java.lang.Thread.sleep;

/**
 * Created by nedp on 28/04/16.
 */
class LiftPath implements Resource<Car> {

    private final MultiResource<Car> multiResource;
    private final int putIndex;
    private final int getIndex;

    LiftPath(MultiResource<Car> multiResource,
             int putIndex, int getIndex) {
        this.multiResource = multiResource;
        this.putIndex = putIndex;
        this.getIndex = getIndex;
    }

    @Override
    public void makeAvailable() throws InterruptedException {
        multiResource.putEmpty(getIndex);
    }

    @Override
    public void reserveItem() throws InterruptedException {
        this.multiResource.waitForFull(getIndex);
    }

    @Override
    public void put(Car item) throws InterruptedException {
        if (putIndex == 0) {
            Logger.logEvent("%s enters %s to go up", item, this);
        } else {
            Logger.logEvent("%s enters %s to go down", item, this);
        }
        multiResource.put(putIndex, getIndex, item);
    }

    @Override
    public void reserveAvailability() throws InterruptedException {
        multiResource.getEmpty(putIndex);
    }

    @Override
    public Car getNow() {
        final Car item = this.multiResource.getNow(getIndex);
        Logger.logEvent("%s exits %s", item, this);
        return item;
    }

    @Override
    public String toString() {
        return multiResource.toString();
    }
}
