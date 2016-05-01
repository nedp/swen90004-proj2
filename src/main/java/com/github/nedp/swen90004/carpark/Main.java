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
        final MultiResource<Car> lift = new MultiResource<>(LEVELS, "lift");

        // Create a list of car park spaces
        final List<Section> sections = new ArrayList<>(2);

        // Generate the individual sections
        for (Integer i = 0; i < Param.SECTIONS; i += 1) {
            sections.add(new Section(i));
        }

        // Generate the producer, the consumer and the operator
        final Entrance entrance = new Entrance();
        final Exit<Car> exit = new Exit<>();
        final Operator operator = new Operator(lift);

        // Create the entrances and exits to the lift.
        final Resource<Car> raiser = new LiftPath(lift, 0, LEVELS - 1);
        final Resource<Car> lowerer = new LiftPath(lift, LEVELS - 1, 0);

        // Create a list of towing vehicles
        final List<DelayedForwarder<Car>> vehicles =
                new ArrayList<>(Param.SECTIONS + 3);

        // Generate the towing vehicle which moves new arrivals into the lift.
        vehicles.add(new DelayedForwarder<>("arrivals", entrance, raiser));

        // Generate the towing vehicle which launches cars from the lift.
        vehicles.add(new DelayedForwarder<>(
                "launcher", raiser, sections.get(0)));

        // Generate the regular towing vehicles.
        for (int i = 1; i < Param.SECTIONS; i += 1) {
            vehicles.add(new DelayedForwarder<>(
                    i, sections.get(i - 1), sections.get(i)));
        }

        // Generate the towing vehicle which loads cars into the lift on
        // the upper level.
        vehicles.add(new DelayedForwarder<>(
                "loader", sections.get(Param.SECTIONS - 1), lowerer));

        // Generate the towing vehicle which removes cars from the lift
        // so they may depart.
        vehicles.add(new DelayedForwarder<>("departures", lowerer, exit));

        // Start up all the vehicles and the operator.
        for (final DelayedForwarder vehicle : vehicles) {
            vehicle.start();
        }
        operator.start();

        // Until the operator or one of the vehicles die,
        // keep drawing new frames.
        boolean vehiclesAreAlive = true;
        while (operator.isAlive() && vehiclesAreAlive) {
            // Have a delay between frames.
            try {
                Thread.sleep(Param.MAIN_INTERVAL);
            }
            catch (InterruptedException e) {
                System.out.println("Main was interrupted");
                break;
            }

            // Draw the state.
            drawUpperLevel(lift, vehicles, sections);
            drawLowerLevel(lift, vehicles, entrance);

            // Check that all vehicles are alive after drawing the state.
            for (final DelayedForwarder vehicle : vehicles) {
                 vehiclesAreAlive &= vehicle.isAlive();
            }
        }

        // If some thread died, interrupt all other threads and halt
        operator.interrupt();

        for (final DelayedForwarder vehicle : vehicles) {
            vehicle.interrupt();
        }

        System.out.println("Main terminates, all threads terminated");
        System.exit(0);
    }

    private static void drawUpperLevel(MultiResource<Car> lift,
                                       List<DelayedForwarder<Car>> vehicles,
                                       List<Section> sections) {
        Logger.logState("%18s", lift.state(LEVELS-1));
        Logger.logState(vehicles.get(1).state());
        for (int i = 0; i < sections.size(); i += 1) {
            Logger.logState(sections.get(i).state());
            Logger.logState(vehicles.get(i + 2).state());
        }
        Logger.logState("%18s", lift.state(LEVELS-1));
    }

    private static void drawLowerLevel(MultiResource<Car> lift,
                                       List<DelayedForwarder<Car>> vehicles,
                                       Entrance entrance) {
        Logger.logState("\n%18s %s %s %73s {exit} <--- %s %18s\n",
                lift.state(0),
                vehicles.get(0).state(),
                entrance.state(),
                "",
                vehicles.get(vehicles.size()-1).state(),
                lift.state(0));
    }

}
