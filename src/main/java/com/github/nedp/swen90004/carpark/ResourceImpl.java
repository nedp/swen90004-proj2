package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class ResourceImpl<T> implements Resource<T> {

    private final int id;
    private final Channel<Void> empty = new Channel<>();
    private final Channel<T> full = new Channel<>();
    private final String getMessage;
    private final String putMessage;

    private T item = null;

    private ResourceImpl(int id, String getMessage, String putMessage) {
        this.id = id;
        this.getMessage = getMessage;
        this.putMessage = putMessage;
        try {
            empty.put(null);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait!", e);
        }
    }

    ResourceImpl(int id) {
        this(id, String.format("leaves section %d", id),
                String.format("enters section %d", id));
    }

    @Override
    public void putEmpty() throws InterruptedException {
        empty.put(null);
        item = null;
    }

    @Override
    public T getNow() {
        final T item = this.item;
        this.item = null;
        Logger.logEvent("%s %s", item, getMessage);
        return item;
    }

    @Override
    public void waitForFull() throws InterruptedException {
        item = this.full.get();
    }

    @Override
    public void put(T item) throws InterruptedException {
        full.put(item);
        this.item = item;
        Logger.logEvent("%s %s", item, putMessage);
    }

    @Override
    public void getEmpty() throws InterruptedException {
        empty.get();
    }

    String state() {
        return String.format("{%4d:%6s}", id, item);
    }
}
