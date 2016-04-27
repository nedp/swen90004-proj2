import java.util.Random;

/**
 * Parameters that influence the behaviour of the system.
 */

public class Param {

  //the number of car park spaces
  public final static int SECTIONS = 6;

  //the time interval at which Main checks threads are alive
  public final static int MAIN_INTERVAL = 50;

  //the time it takes to operate the lift
  public final static int OPERATE_TIME = 800;

  //the time it takes to tow
  public final static int TOWING_TIME = 1200;

  //the maximum amount of time between car arrivals
  public final static int MAX_ARRIVE_INTERVAL = 2400;

  //the maximum amount of time between car departures
  public final static int MAX_DEPART_INTERVAL = 800;

  //the maximum amount of time between operating the lift
  public final static int MAX_OPERATE_INTERVAL = 6400;

/**
 * For simplicity, we assume uniformly distributed time lapses.
 * An exponential distribution might be a fairer assumption.
 */

  public static int arrivalLapse() {
    Random random = new Random();
    return random.nextInt(MAX_ARRIVE_INTERVAL);
  }

  public static int departureLapse() {
    Random random = new Random();
    return random.nextInt(MAX_DEPART_INTERVAL);
  }

  public static int operateLapse() {
    Random random = new Random();
    return random.nextInt(MAX_OPERATE_INTERVAL);
  }
}

