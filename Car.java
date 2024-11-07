import java.util.concurrent.TimeUnit;

// Class representing a car attempting to park
class Car implements Runnable
{
    private final int id;               
    private final String gate;          
    private final int arrivalTime;      
    private final int parkDuration;     
    private final CarParkManager parkingSystem; 

    // Constructor to initialize the car attributes
    public Car(String gate, int id, int arrivalTime, int parkDuration, CarParkManager parkingSystem) {
        this.gate = gate;
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.parkDuration = parkDuration;
        this.parkingSystem = parkingSystem;
    }

    // Getter methods for encapsulated properties
    public int getCarID() {
        return id;
    }

    public String getGateID() {
        return gate;
    }

    public int getParkingDuration() {
        return parkDuration;
    }

    // Runnable method defining the car's behavior in the parking system
    @Override
    public void run() {
        try {
            // Simulate car's arrival after a delay
            Thread.sleep(arrivalTime * 1000);
            System.out.printf("Car %d from %s arrived after %d seconds.\n", id, gate, arrivalTime);

            // Attempt parking and wait for the designated parking duration if successful
            if (parkingSystem.attemptParking(this)) {
                Thread.sleep(parkDuration * 1000);
                parkingSystem.exitParking(this);
            } else {
                System.out.printf("Car %d from %s could not find a parking spot.\n", id, gate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
