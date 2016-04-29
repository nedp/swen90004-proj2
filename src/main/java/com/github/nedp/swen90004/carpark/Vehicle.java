package com.github.nedp.swen90004.carpark;

/**
 * Created by nedp on 27/04/16.
 */
class Vehicle<T> extends Thread {
    private final String id;
    private final Producer<T> source;
    private final Consumer<T> destination;

    Vehicle(String id, Producer<T> source, Consumer<T> destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }

    Vehicle(int id, Producer<T> source, Consumer<T> destination) {
        this(Integer.toString(id), source, destination);
    }

    @Override
    public void run() {
        while (true) {
            try {
                final T item = source.get();
                destination.getEmpty();
                sleep(Param.TOWING_TIME);
                destination.put(item);
                source.putEmpty();
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format(
                        "Vehicle %s stopped; it was interrupted!", id), e);
            }
        }
    }
}
