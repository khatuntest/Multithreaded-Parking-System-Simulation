import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader("Cars"));
                PrintWriter writer = new PrintWriter(new FileWriter("output"))
        ) {
            // Initialize gates
            Gate gate1 = new Gate("Gate 1", 4); // Assuming 4 parking spots
            Gate gate2 = new Gate("Gate 2", 4);
            Gate gate3 = new Gate("Gate 3", 4);

            ArrayList<Car> cars = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                String gateName = parts[0].split(" ")[1];
                int carId = Integer.parseInt(parts[1].split(" ")[1]);
                int arriveTime = Integer.parseInt(parts[2].split(" ")[1]);
                int parkDuration = Integer.parseInt(parts[3].split(" ")[1]);

                // Select the correct gate based on the gate name
                Gate gate = switch (gateName) {
                    case "1" -> gate1;
                    case "2" -> gate2;
                    case "3" -> gate3;
                    default -> throw new IllegalArgumentException("Invalid gate number: " + gateName);
                };

                cars.add(new Car(carId, arriveTime, parkDuration, gate, writer));
            }

            // Start all car threads
            for (Car car : cars) {
                car.start();
            }

            // Wait for all car threads to finish
            for (Car car : cars) {
                try {
                    car.join();
                } catch (InterruptedException e) {
                    System.out.println("Main thread interrupted.");
                }
            }

            // Write summary information to output
            writer.println("\nTotal Cars Served: " + (gate1.getCarsServed() + gate2.getCarsServed() + gate3.getCarsServed()));
            writer.println("Current Cars in Parking: " + (gate1.getCurrentCars() + gate2.getCurrentCars() + gate3.getCurrentCars()));
            writer.println("Details:");
            writer.println("- " + gate1.getGateName() + " served " + gate1.getCarsServed() + " cars.");
            writer.println("- " + gate2.getGateName() + " served " + gate2.getCarsServed() + " cars.");
            writer.println("- " + gate3.getGateName() + " served " + gate3.getCarsServed() + " cars.");

        } catch (IOException e) {
            System.out.println("Error reading or writing files: " + e.getMessage());
        }
    }
}
