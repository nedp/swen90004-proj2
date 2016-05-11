package com.github.nedp.swen90004.carpark;

import java.util.ArrayList;
import java.util.List;

/**
 * The top-level component of the carpark system simulator.
 */
class Main {

    // The number of levels (floors) in the capark, including the ground floor.
    private static final int LEVELS = 2;

    // Generate the lift
    private final Lift<Car> lift = new Lift<>(LEVELS, "lift");

    // Create a list of car park spaces
    private final List<Section> sections = new ArrayList<>(2);

    // Generate the entrance of the carpark and the lift operator.
    private final Entrance entrance = new Entrance();
    private final Operator operator = new Operator<>(lift);

    // Create a list of towing vehicles
    private final List<Vehicle<Car>> vehicles =
            new ArrayList<>(Param.SECTIONS + 3);

    private Main() {
        // Create the exit of the carpark.
        final Exit<Car> exit = new Exit<>();

        // Create the entrances and exits to the lift.
        final Resource<Car> raiser = new LiftResource(lift, 0, LEVELS - 1);
        final Resource<Car> lowerer = new LiftResource(lift, LEVELS - 1, 0);

        // Generate the individual sections
        for (Integer i = 0; i < Param.SECTIONS; i += 1) {
            sections.add(new Section(i));
        }

        // Generate the towing vehicle which moves new arrivals into the lift.
        vehicles.add(new Vehicle<>("arrivals", entrance, raiser));

        // Generate the towing vehicle which launches cars from the lift.
        vehicles.add(new Vehicle<>(
                "launcher", raiser, sections.get(0)));

        // Generate the regular towing vehicles.
        for (int i = 1; i < Param.SECTIONS; i += 1) {
            vehicles.add(new Vehicle<>(
                    i, sections.get(i - 1), sections.get(i)));
        }

        // Generate the towing vehicle which loads cars into the lift on
        // the upper level.
        vehicles.add(new Vehicle<>(
                "loader", sections.get(Param.SECTIONS - 1), lowerer));

        // Generate the towing vehicle which removes cars from the lift
        // so they may depart.
        vehicles.add(new Vehicle<>("departures", lowerer, exit));
    }

    private void start() {
        // Start up all the vehicles and the operator.
        for (final Vehicle<Car> vehicle : vehicles) {
            vehicle.start();
        }
        operator.start();
    }

    // Checks whether all threads in the simulation are alive.
    private boolean isAlive() {
        // The simulation is dead if the operator is dead.
        if (!operator.isAlive()) return false;

        // The simulation is dead if any of the vehicles are dead.
        for (final Vehicle<Car> vehicle : vehicles) {
            if (!vehicle.isAlive()) return false;
        }
        return true;
    }

    // Draws the state.
    private void draw() {
        // Draws the state in the upper level.
        Logger.logState("%18s", lift.state(LEVELS-1));
        Logger.logState(vehicles.get(1).state());
        for (int i = 0; i < sections.size(); i += 1) {
            Logger.logState(sections.get(i).state());
            Logger.logState(vehicles.get(i + 2).state());
        }
        Logger.logState("%18s\n", lift.state(LEVELS-1));

        // Draws the state in the lower level.
        // Optimise spacing for 6 sections.
        Logger.logState("%18s %s %s %73s {exit} <--- %s %18s\n",
                lift.state(0),
                vehicles.get(0).state(),
                entrance.state(),
                "",
                vehicles.get(vehicles.size()-1).state(),
                lift.state(0));
    }

    private void interrupt() {
        operator.interrupt();
        for (final Vehicle<Car> vehicle : vehicles) {
            vehicle.interrupt();
        }
    }

    /**
     * The driver of the lift/carpark system:
     *  - creates the components for the system;
     *  - starts all of the processes;
     *  - periodically tells components to draw themselves;
     *  - supervise processes regularly to check all are alive.
     */
    public static void main(String [] args) {
        // Construct the simulation.
        final Main main = new Main();

        // Start the simulation.
        main.start();

        // Every frame, check that the simulation is still alive,
        // then draw all the state.
        while (main.isAlive()) {
            // Draw the state.
            main.draw();
            // Have a delay between frames.
            try {
                Thread.sleep(Param.MAIN_INTERVAL);
            } catch (InterruptedException e) {
                System.out.println("Main was interrupted");
                break;
            }
        }

        // If we're here, main is no longer alive and some thread was
        // interrupted.
        // Interrupt all the threads and halt.
        main.interrupt();
        System.out.println("Main terminates, all threads terminated");
        System.exit(0);
    }
}
