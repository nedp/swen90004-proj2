package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Section implements Resource<Car> {

    private final int id;
    private final NullChannel empty = new NullChannel();
    private final Channel<Car> full = new Channel<>();
    private final String getMessage;
    private final String putMessage;

    private Car item = null;

    private Section(int id, String getMessage, String putMessage) {
        this.id = id;
        this.getMessage = getMessage;
        this.putMessage = putMessage;
        try {
            empty.put();
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait!", e);
        }
    }

    Section(int id) {
        this(id, String.format("leaves section %d", id),
                String.format("enters section %d", id));
    }

    @Override
    public void makeAvailable() throws InterruptedException {
        empty.put();
    }

    @Override
    public Car getNow() {
        final Car item = this.item;
        this.item = null;
        Logger.logEvent("%s %s", item, getMessage);
        return item;
    }

    @Override
    public void reserveItem() throws InterruptedException {
        item = this.full.get();
    }

    @Override
    public void put(Car item) throws InterruptedException {
        full.put(item);
        this.item = item;
        Logger.logEvent("%s %s", item, putMessage);
    }

    @Override
    public void reserveAvailability() throws InterruptedException {
        empty.get();
    }

    String state() {
        return String.format("{%4d:%6s}", id, item);
    }
}
