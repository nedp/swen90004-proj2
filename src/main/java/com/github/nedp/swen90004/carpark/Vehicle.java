package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Vehicle<T> extends Thread {
    private final String id;
    private final Producer<T> source;
    private final Consumer<T> destination;

    private T item = null;

    Vehicle(String id, Producer<T> source, Consumer<T> destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }

    Vehicle(int id, Producer<T> source, Consumer<T> destination) {
        this(Integer.toString(id), source, destination);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                source.waitForFull();
                destination.getEmpty();
                item = source.getNow();
                sleep(Param.TOWING_TIME);
                destination.put(item);
                item = null;
                source.putEmpty();
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format(
                        "Vehicle %s stopped; it was interrupted!", id), e);
            }
        }
    }

    String state() {
        return String.format("%6s", (item == null) ? "" : item);
    }
}
