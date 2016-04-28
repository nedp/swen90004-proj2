package com.github.nedp.swen90004.carpark;

import java.util.ArrayList;
import java.util.List;

/**
 * The top-level component of the carpark system simulator.
 */
class Main {

    static final int LEVELS = 2;

    /**
     * The driver of the lift/carpark system:
     *  - create the components for the system;
     *  - start all of the processes;
     *  - supervise processes regularly to check all are alive.
     */
    public static void main(String [] args) {
        // Generate the lift
        final MultiResource<Car> lift = new MultiResource<>(LEVELS);

        // Create a list of car park spaces
        final List<Section<Car>> sections = new ArrayList<>(2);

        // Generate the individual sections
        for (Integer i = 0; i < Param.SECTIONS; i += 1) {
            sections.add(new Section<>(i));
        }

        // Generate the producer, the consumer and the operator
        final Producer<Car> entrance = new Entrance();
        final Consumer<Car> exit = new Exit();
        final Operator operator = new Operator(lift);

        // Create the entrances and exits to the lift.
        final Resource<Car> raisingLift =
                new Lift<>(lift, 0, LEVELS - 1);
        final Resource<Car> loweringLift =
                new Lift<>(lift, LEVELS - 1, 0);

        // Create a list of towing vehicles
        final List<Vehicle<Car>> vehicles = new ArrayList<>(Param.SECTIONS + 3);

        // Generate the towing vehicle which moves new arrivals into the lift.
        vehicles.add(
                new Vehicle<>("arrivals", entrance, raisingLift));

        // Generate the towing vehicle which launches cars from the lift.
        vehicles.add(
                new Vehicle<>("launcher", raisingLift, sections.get(0)));

        // Generate the regular towing vehicles.
        for (int i = 1; i < Param.SECTIONS; i += 1) {
            vehicles.add(
                    new Vehicle<>(i, sections.get(i - 1), sections.get(i)));
        }

        // Generate the towing vehicle which loads cars into the lift on
        // the upper level.
        vehicles.add(new Vehicle<>("loader",
                sections.get(Param.SECTIONS - 1), loweringLift));

        // Generate the towing vehicle which removes cars from the lift
        // so they may depart.
        vehicles.add(new Vehicle<>("departures", loweringLift, exit));

        // Start up all the vehicles and the operator.
        for (final Vehicle vehicle : vehicles) {
            vehicle.start();
        }
        operator.start();

        // Regularly check on the status of threads
        boolean vehiclesAreAlive = true;
        while (operator.isAlive() && vehiclesAreAlive) {
            try {
                Thread.sleep(Param.MAIN_INTERVAL);
            }
            catch (InterruptedException e) {
                System.out.println("Main was interrupted");
                break;
            }
            for (final Vehicle vehicle : vehicles) {
                 vehiclesAreAlive &= vehicle.isAlive();
            }
        }

        // If some thread died, interrupt all other threads and halt
        operator.interrupt();

        for (final Vehicle vehicle : vehicles) {
            vehicle.interrupt();
        }

        System.out.println("Main terminates, all threads terminated");
        System.exit(0);
    }
}
