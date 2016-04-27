/**
 * A class whose instances are Car, each with its unique Id.
 */

public class Car {

  //the Id of this car
  protected int id;

  //the next ID to be allocated
  protected static int nextId = 1;

  //create a new car with a given Id
  protected Car(int id) {
    this.id = id;
  }

  //get a new car instance with a unique Id
  public static Car getNewCar() {
    return new Car(nextId++);
  }

  //produce the Id of this car
  public int getId() {
    return id;
  }

  //produce an identifying string for the car
  public String toString() {
    return "[" + id + "] ";
  }
}

