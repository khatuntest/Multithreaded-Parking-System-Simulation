package org.example;

public class Main {
    public static void main(String[] args) {
        // Create an instance of the SJF scheduler
        SJF sjfScheduler = new SJF();

        // Create processes
        Process p1 = new Process(1, 0, 8); // PID=1, Arrival=0, Burst=8
        Process p2 = new Process(2, 1, 4); // PID=2, Arrival=1, Burst=4
        Process p3 = new Process(3, 2, 9); // PID=3, Arrival=2, Burst=9
        Process p4 = new Process(4, 3, 5); // PID=4, Arrival=3, Burst=5

        // Insert processes into the SJF scheduler
        sjfScheduler.insert_process(p1);
        sjfScheduler.insert_process(p2);
        sjfScheduler.insert_process(p3);
        sjfScheduler.insert_process(p4);

        // Run the SJF scheduling with context switch
        sjfScheduler.context_switch();
    }
}
