/**
 * The top-level component of the carpark system simulator.
 */ 

public class Main {

  /**
   * The driver of the lift/carpark system:
   *  - create the components for the system; 
   *  - start all of the processes; 
   *  - supervise processes regularly to check all are alive.
   */

  public static void main(String [] args) {
    int n = Param.SECTIONS;

    //generate the lift
    Lift lift = new Lift();

    //create an array sec to hold the car park spaces
    Section[] sec = new Section[n];

    //generate the individual sections
    for (int i = 0; i < n; i++) {
      sec[i] = new Section(i);
    }

    //generate the producer, the consumer and the operator
    Producer producer = new Producer(lift);
    Consumer consumer = new Consumer(lift);
    Operator operator = new Operator(lift);

    ////create an array sec to hold the towing vehicles
    Vehivle[] vehicle = new Vehicle[n-1];

    //generate the individual towing vehicles
    for (int i = 0; i < n-1; i++) {
      vehicle[i] = new Vehicle(i,sec[i],sec[i+1]);
      vehicle[i].start();
    }

    //generate special towing vehicles that have access to the lift
    Launch_vehicle launch_vehicle = new Launch_vehicle(lift,sec[0]);
    Return_vehicle return_vehicle = new Return_vehicle(sec[n-1],lift);

    //start up all the components
    launch_vehicle.start();
    return_vehicle.start();
    producer.start();
    consumer.start();
    operator.start();

    //regularly check on the status of threads
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

    //if some thread died, interrupt all other threads and halt
    producer.interrupt();    
    consumer.interrupt();
    operator.interrupt();

    for (int i = 0; i < n-1; i++) {
      vehicle[i].interrupt();
    }
    launch_vehicle.interrupt();
    return_vehicle.interrupt();

    System.out.println("Main terminates, all threads terminated");
    System.exit(0);
  }
}
