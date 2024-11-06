package org.example;

import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.TreeMap;

public class Parking_System {
    public static Semaphore s;
    public final Map<Integer, Integer> gates;
    private int currentCarsInParking;


    Parking_System() {
        s = new Semaphore(4);
        gates = new TreeMap<>();
        currentCarsInParking = 0;
    }


    public synchronized void increment_gate(int gate) {
        gates.put(gate, gates.getOrDefault(gate, 0) + 1);
    }


    public synchronized void carParked() {
        currentCarsInParking++;
    }


    public synchronized void carLeft() {
        currentCarsInParking--;
    }


    public void park(String car_name, int arrive, int parks, int gate) {
        try {
            long waitStartTime = System.currentTimeMillis();
            System.out.println(car_name + " from Gate " + gate + " arrived at time " + arrive);


            if (s.tryAcquire()) {

                carParked();
                System.out.println(car_name + " from Gate " + gate + " parked. (Parking Status: " + currentCarsInParking + " spots occupied)");
            } else {

                System.out.println(car_name + " from Gate " + gate + " waiting for a spot.");
                long waitTime = 0;

                while (!s.tryAcquire()) {
                    waitTime++;
                    Thread.sleep(1000);
                }

                carParked();
                System.out.println(car_name + " from Gate " + gate + " parked after waiting for " + waitTime + " units of time. (Parking Status: " + currentCarsInParking + " spots occupied)");
            }


            Thread.sleep(parks * 1000L);


            leave(car_name, waitStartTime, gate);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    
    public void leave(String car_name, long waitStartTime, int gate) {
        s.release();
        carLeft();
        long parkingTime = (System.currentTimeMillis() - waitStartTime) / 1000;
        System.out.println(car_name + " from Gate " + gate + " left after " + parkingTime + " units of time. (Parking Status: " + currentCarsInParking + " spots occupied)");
        increment_gate(gate);
    }


    public int getCurrentCarsInParking() {
        return currentCarsInParking;
    }
}
