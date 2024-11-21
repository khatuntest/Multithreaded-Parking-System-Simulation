import java.util.LinkedList;
import java.util.Queue;

class Car implements Runnable {
    private final int id;
    private final String gate;
    private final int arrivalTime;
    private final int parkDuration;
    private final CarParkManager parkingSystem;

    public Car(String gate, int id, int arrivalTime, int parkDuration, CarParkManager parkingSystem) {
        this.gate = gate;
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.parkDuration = parkDuration;
        this.parkingSystem = parkingSystem;
    }

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

    public int getCarID() {
        return id;
    }

    public String getGateID() {
        return gate;
    }

    public int getParkingDuration() {
        return parkDuration;
    }
}
