package com.github.nedp.swen90004.carpark;

import java.util.Optional;

/**
 * Created by nedp on 27/04/16.
 */
class Section<T> implements Resource<T> {

    private final int id;
    private final Channel<Void> empty = new Channel<>();
    private final Channel<T> full = new Channel<>();
    private final String getMessage;
    private final String putMessage;

    Section(int id, String getMessage, String putMessage) {
        this.id = id;
        this.getMessage = getMessage;
        this.putMessage = putMessage;
        try {
            empty.put(null);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unexpected wait!", e);
        }
    }

    Section(int id) {
        this(id, String.format("leaves section %d", id),
                String.format("enters section %d", id));
    }

    @Override
    public void putEmpty() throws InterruptedException {
        this.empty.put(null);
    }

    @Override
    public void put(T item) throws InterruptedException {
        this.full.put(item);
    }

    @Override
    public void getEmpty() throws InterruptedException {
        this.empty.get();
    }

    @Override
    public String putMessage() {
        return putMessage;
    }

    @Override
    public T get() throws InterruptedException {
        return this.full.get();
    }

    @Override
    public Optional<String> getMessage() {
        return Optional.of(getMessage);
    }
}
