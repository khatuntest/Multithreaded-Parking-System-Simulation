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

        Process p5 = new Process(5, 0, 6); // Process 1 arrives at time 0 with burst 6
        Process p6 = new Process(6, 2, 4); // Process 2 arrives at time 2 with burst 4
        Process p7 = new Process(7, 4, 5); // Process 3 arrives at time 4 with burst 5
        Process p8 = new Process(8, 6, 2); // Process 4 arrives at time 6 with burst 2
        Process p9 = new Process(9, 8, 3); // Process 5 arrives at time 8 with burst 3

        // Insert processes into the scheduler
        sjfScheduler.insert_process(p5);
        sjfScheduler.insert_process(p6);
        sjfScheduler.insert_process(p7);
        sjfScheduler.insert_process(p8);
        sjfScheduler.insert_process(p9);

        // Run the SJF scheduling with context switch
        sjfScheduler.context_switch();
    }
}
