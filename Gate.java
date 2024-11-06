import java.util.concurrent.Semaphore;

public class Gate {
    private final String gateName;
    private final Semaphore parkingSem;
    private int currentCars;
    private int carsServed;

    public Gate(String gateName, int totalSpots) {
        this.gateName = gateName;
        this.parkingSem = new Semaphore(totalSpots, true);
        this.currentCars = 0;
        this.carsServed = 0;
    }

    public String getGateName() {
        return gateName;
    }

    public synchronized boolean parkCar(Car car) {
        if (parkingSem.tryAcquire()) {
            currentCars++;
            carsServed++;
            return true;
        }
        return false; // No available parking spots
    }

    public synchronized void removeCar() {
        currentCars--;
        parkingSem.release();
    }

    public int getCarsServed() {
        return carsServed;
    }

    public int getCurrentCars() {
        return currentCars;
    }
}
