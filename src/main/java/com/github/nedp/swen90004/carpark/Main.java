package com.github.nedp.swen90004.carpark;

/**
 * The top-level component of the carpark system simulator.
 */
class Main {

    /**
     * The driver of the lift/carpark system:
     *  - create the components for the system;
     *  - start all of the processes;
     *  - supervise processes regularly to check all are alive.
     */
    public static void main(String [] args) {
        final int n = Param.SECTIONS;

        // Generate the lift
        final Lift lift = new Lift();

        // Create an array sec to hold the car park spaces
        final Section[] sec = new Section[n];

        // Generate the individual sections
        for (int i = 0; i < n; i += 1) {
            sec[i] = new Section(i);
        }

        // Generate the producer, the consumer and the operator
        final Producer producer = new Producer(lift);
        final Consumer consumer = new Consumer(lift);
        final Operator operator = new Operator(lift);

        // Create an array sec to hold the towing vehicles
        Vehicle[] vehicle = new Vehicle[n-1];

        // Generate the individual towing vehicles
        for (int i = 0; i < n-1; i++) {
            vehicle[i] = new Vehicle(i,sec[i],sec[i+1]);
            vehicle[i].start();
        }

        // Generate special towing vehicles that have access to the lift
        final LaunchVehicle launchVehicle = new LaunchVehicle(lift, sec[0]);
        final ReturnVehicle returnVehicle = new ReturnVehicle(sec[n-1], lift);

        // Start up all the components
        launchVehicle.start();
        returnVehicle.start();
        producer.start();
        consumer.start();
        operator.start();

        // Regularly check on the status of threads
        boolean vehicles_alive = true;
        for (int i = 0; i < n-1; i++) {
            vehicles_alive = vehicles_alive && vehicle[i].isAlive();
        }
        while (producer.isAlive() && consumer.isAlive()
                && operator.isAlive() && vehicles_alive) {
            try {
                Thread.sleep(Param.MAIN_INTERVAL);
            }
            catch (InterruptedException e) {
                System.out.println("Main was interrupted");
                break;
            }
            for (int i = 0; i < n-1; i++) {
                vehicles_alive = vehicles_alive && vehicle[i].isAlive();
            }
        }

        // If some thread died, interrupt all other threads and halt
        producer.interrupt();
        consumer.interrupt();
        operator.interrupt();

        for (int i = 0; i < n-1; i++) {
            vehicle[i].interrupt();
        }
        launchVehicle.interrupt();
        returnVehicle.interrupt();

        System.out.println("Main terminates, all threads terminated");
        System.exit(0);
    }
}
