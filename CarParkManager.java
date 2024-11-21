import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import java.util.HashMap;
import java.util.Map;

public class CarParkManager {
    private static final int MAX_SPACES = 4;  // Maximum parking spaces available
    private final semaphore availableSpaces = new semaphore(MAX_SPACES); // Custom semaphore
    private int currentOccupancy = 0; // Cars currently parked
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

        // Try to acquire a parking spot, with a 5-second timeout (simulated)
        long startTime = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - startTime < 5000) { // 5-second timeout
                synchronized (availableSpaces) {
                    if (availableSpaces.value > 0) {
                        availableSpaces.P(); // Acquire a permit
                        synchronized (this) { // Synchronize to update shared resources safely
                            currentOccupancy++;
                            totalCarsProcessed++;
                            carsByGate.put(gateID, carsByGate.get(gateID) + 1); // Increment cars for the specific gate
                        }

                        // Confirmation message when a car parks successfully
                        System.out.printf("Car %d from %s parked. (Status: %d of %d occupied)\n",
                                car.getCarID(), gateID, MAX_SPACES - availableSpaces.value, MAX_SPACES);
                        return true;
                    }
                }
                // Sleep briefly to avoid tight loop while waiting for a parking space
                Thread.sleep(100);
            }
            // If no spot was found in time, inform the user
            System.out.printf("Car %d from %s was unable to park within the time limit.\n", car.getCarID(), gateID);
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    // Method for a car to leave the parking
    public void exitParking(Car car) {
        synchronized (availableSpaces) {
            availableSpaces.V(); // Free up a spot in the parking lot
        }

        synchronized (this) { // Synchronize access to shared resources
            currentOccupancy--; // Decrease current occupancy count
        }

        // Notify about car departure
        System.out.printf("Car %d from %s departed after %d seconds. (Status: %d of %d occupied)\n",
                car.getCarID(), car.getGateID(), car.getParkingDuration(), MAX_SPACES - availableSpaces.value, MAX_SPACES);
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
