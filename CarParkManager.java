import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class CarParkManager {
    private static final int MAX_SPACES = 4;  // Maximum parking spaces available
    private final Semaphore availableSpaces = new Semaphore(MAX_SPACES, true); // Semaphore to control space access
    private int currentOccupancy = 0;    // Cars currently parked
    private int totalCarsProcessed = 0; // Total number of cars processed

    // Map to track cars served at each gate, identified by gate number
    private final Map<String, Integer> carsByGate = new HashMap<>();

    // Constructor to initialize gate counters
    public CarParkManager() {
        carsByGate.put("Gate 1", 0);
        carsByGate.put("Gate 2", 0);
        carsByGate.put("Gate 3", 0);
    }

    // Method for a car to attempt parking
    public boolean attemptParking(Car car) {
        String gateID = car.getGateID();

        // Validate that the gate exists in the tracking map
        if (!carsByGate.containsKey(gateID)) {
            System.out.printf("Warning: %s not recognized in system.\n", gateID);
            return false;
        }

        // Try to acquire a parking spot, with a 5-second timeout
        try {
            if (availableSpaces.tryAcquire(5, java.util.concurrent.TimeUnit.SECONDS)) {
                synchronized (this) { // Synchronize to update shared resources safely
                    currentOccupancy++;       // Increase current occupancy
                    totalCarsProcessed++;     // Increase total cars processed count
                    carsByGate.put(gateID, carsByGate.get(gateID) + 1); // Increment cars for the specific gate
                }

                // Confirmation message when a car parks successfully
                System.out.printf("Car %d from %s parked. (Status: %d of %d occupied)\n",
                        car.getCarID(), gateID, MAX_SPACES - availableSpaces.availablePermits(), MAX_SPACES);
                return true;
            } else {
                // If no spot was found in time, inform the user
                System.out.printf("Car %d from %s was unable to park within the time limit.\n", car.getCarID(), gateID);
                return false;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    // Method for a car to leave the parking
    public void exitParking(Car car) {
        availableSpaces.release(); // Free up a spot in the parking lot

        synchronized (this) { // Synchronize access to shared resources
            currentOccupancy--; // Decrease current occupancy count
        }

        // Notify about car departure
        System.out.printf("Car %d from %s departed after %d seconds. (Status: %d of %d occupied)\n",
                car.getCarID(), car.getGateID(), car.getParkingDuration(), MAX_SPACES - availableSpaces.availablePermits(), MAX_SPACES);
    }

    // Method to display the current state and summary to a file
    public void displayReport(PrintStream writer) throws IOException {
        writer.println("\nSummary:");
        writer.println("Total Cars Processed: " + totalCarsProcessed);
        writer.println("Current Occupancy: " + currentOccupancy);

        writer.println("Cars Served per Gate:");
        for (Map.Entry<String, Integer> entry : carsByGate.entrySet()) {
            writer.printf("- %s handled %d cars.\n", entry.getKey(), entry.getValue());
        }
    }
}

