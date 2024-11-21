import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CarParkManager
{
    private static final int MAX_SPACES = 4; // Maximum parking spaces
    private final Queue<Car> waitingQueue = new LinkedList<>(); // Queue for FIFO enforcement
    private int currentOccupancy = 0; // Cars currently parked
    private int totalCarsProcessed = 0; // Total cars processed

    private final Map<String, Integer> carsByGate = new HashMap<>(); // Track cars served per gate

    public CarParkManager()
    {
        carsByGate.put("Gate 1", 0);
        carsByGate.put("Gate 2", 0);
        carsByGate.put("Gate 3", 0);
    }

    // Car attempts to park
    public synchronized boolean attemptParking(Car car) {
        waitingQueue.add(car); // Add car to the queue
        while (waitingQueue.peek() != car || currentOccupancy >= MAX_SPACES)
        {
            try
            {
                wait(); // Wait until conditions are met
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // Car is allowed to park
        waitingQueue.poll(); // Remove car from the queue
        currentOccupancy++; // Increase occupancy
        totalCarsProcessed++;
        carsByGate.put(car.getGateID(), carsByGate.get(car.getGateID()) + 1);

        System.out.printf("Car %d from %s parked. (Status: %d of %d occupied)\n",
                car.getCarID(), car.getGateID(), currentOccupancy, MAX_SPACES);
        return true;
    }

    // Car leaves the parking
    public synchronized void exitParking(Car car)
    {
        currentOccupancy--; // Decrease occupancy
        System.out.printf("Car %d from %s departed after %d seconds. (Status: %d of %d occupied)\n",
                car.getCarID(), car.getGateID(), car.getParkingDuration(), currentOccupancy, MAX_SPACES);
        notifyAll(); // Notify waiting cars
    }

    // Display report
    public void displayReport(PrintStream writer) {
        writer.println("\nSummary:");
        writer.println("Total Cars Processed: " + totalCarsProcessed);
        writer.println("Current Occupancy: " + currentOccupancy);

        writer.println("Cars Served per Gate:");
        for (Map.Entry<String, Integer> entry : carsByGate.entrySet()) {
            writer.printf("- %s handled %d cars.\n", entry.getKey(), entry.getValue());
        }
    }
}
