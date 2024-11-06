package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Parking_System parkingSystem = new Parking_System();
        List<car> cars = new ArrayList<>();


        try {
            BufferedReader r = new BufferedReader(new FileReader("Cars"));
            String line = r.readLine();

            while (line != null) {
                String[] subs = line.split(",");
                int gate = Integer.parseInt(subs[0].trim().split(" ")[1].trim());
                String car = subs[1].trim();
                int arrive = Integer.parseInt(subs[2].trim().split(" ")[1].trim());
                int parks = Integer.parseInt(subs[3].trim().split(" ")[1].trim());
                cars.add(new car(car, arrive, parks, gate, parkingSystem));
                line = r.readLine();
            }
            r.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading the car data", e);
        }


        try {
            List<Thread> threads = new ArrayList<>();
            for (car Car : cars) {
                Thread thread = new Thread(Car);
                threads.add(thread);
                thread.start();
            }


            for (Thread thread : threads) {
                thread.join();
            }


            System.out.println("\nTotal Cars Served: " + cars.size());
            System.out.println("Current Cars in Parking: " + parkingSystem.getCurrentCarsInParking());

            System.out.println("Details:");
            for (Map.Entry<Integer, Integer> entry : parkingSystem.gates.entrySet()) {
                System.out.println("- Gate " + entry.getKey() + " served " + entry.getValue() + " cars.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during parking process", e);
        }
    }
}
