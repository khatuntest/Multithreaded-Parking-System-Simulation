import java.io.PrintWriter;

public class Car extends Thread {
    private final int id;
    private final int arriveTime;
    private final int parkDuration;
    private final Gate gate;
    private final PrintWriter writer;
    private int waitTime;

    public Car(int id, int arriveTime, int parkDuration, Gate gate, PrintWriter writer) {
        this.id = id;
        this.arriveTime = arriveTime;
        this.parkDuration = parkDuration;
        this.gate = gate;
        this.writer = writer;
        this.waitTime = 0;
    }

    @Override
    public void run() {
        try {
            // Simulate arrival
            Thread.sleep(arriveTime * 1000);
            writer.println("Car " + id + " from " + gate.getGateName() + " arrived at time " + arriveTime);

            // Try to park the car with waiting
            while (!gate.parkCar(this)) {
                writer.println("Car " + id + " from " + gate.getGateName() + " waiting for a spot.");
                waitTime++;
                Thread.sleep(1000); // Wait and retry every second
            }

            if (waitTime > 0) {
                writer.println("Car " + id + " from " + gate.getGateName() + " parked after waiting for " + waitTime + " units of time. (Parking Status: " + gate.getCurrentCars() + " spots occupied)");
            } else {
                writer.println("Car " + id + " from " + gate.getGateName() + " parked. (Parking Status: " + gate.getCurrentCars() + " spots occupied)");
            }

            // Stay parked for the duration
            Thread.sleep(parkDuration * 1000);
            gate.removeCar();
            writer.println("Car " + id + " from " + gate.getGateName() + " left after " + parkDuration + " units of time. (Parking Status: " + gate.getCurrentCars() + " spots occupied)");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
