import java.util.ArrayList;
import java.util.List;

import java.io.*;

public class Main
{
    public static void main(String[] args)
    {
        String inputFileName = "Car.txt";
        String outputFileName = "simulation_report.txt";

        try (PrintStream out = new PrintStream(new FileOutputStream(outputFileName)))
        {
            System.setOut(out);

            CarParkManager parkingSystem = new CarParkManager();

            List<Thread> carThreads = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName)))
            {
                String line;
                System.out.println("Reading car data from file...");

                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("done")) {
                        break;
                    }

                    try
                    {
                        String[] parts = line.split(", ");
                        String gateInput = parts[0];
                        int carId = Integer.parseInt(parts[1].split(" ")[1]);  // Parse the car ID
                        int arrivalTime = Integer.parseInt(parts[2].split(" ")[1]);  // Parse arrival time
                        int parkDuration = Integer.parseInt(parts[3].split(" ")[1]);  // Parse parking duration


                        Car car = new Car(gateInput, carId, arrivalTime, parkDuration, parkingSystem);

                        // Create a new thread to represent the car
                        Thread carThread = new Thread(car);

                        // Add the thread to the list of car threads
                        carThreads.add(carThread);
                    } catch (Exception e) {
                        // If there's an error in parsing, notify the user
                        System.out.println("Invalid format in the file: " + line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading the file: " + e.getMessage());
                return;
            }

            // Print message indicating the simulation is starting
            System.out.println("Starting parking simulation...");

            // Start all car threads to simulate the parking operation
            for (Thread carThread : carThreads) {
                carThread.start();
            }

            // Wait for all car threads to finish their parking operations
            for (Thread carThread : carThreads) {
                try
                {
                    carThread.join();
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            parkingSystem.displayReport(System.out);

        } catch (IOException e) {
            System.out.println("Error setting up output file: " + e.getMessage());
        }
    }
}