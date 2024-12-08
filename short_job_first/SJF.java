package org.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class SJF {
    private final List<Process> processes; // List of all processes
    private int current,complete_processes; // Current time in the processor & Number of completed processes
    // Method to print the scheduling results in a tabular format
    private void print(){
        System.out.println("PID\tArrival\tBurst\tEnd\tTurnaround\tWaiting\tExecuted order");
        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.arrival + "\t\t" + p.burst + "\t\t" +
                    p.end + "\t\t" + p.turnaround + "\t\t" + p.waiting+"\t\t"+ p.executed);
        }
        System.out.println("________________________________________________________________________");
    }
    // initialize the process list and other private attributes
    public SJF(){
        processes=new ArrayList<>();
        current=0;
        complete_processes=0;
    }
    // Method to add a process to the list
    public void insert_process(Process p){
        processes.add(p);
    }
    // Method to execute the context-switching logic for SJF scheduling
    public void context_switch(){
        // Sort processes by their arrival time to handle them in order of arrival
        processes.sort(Comparator.comparing(p->p.arrival));
        boolean[] finished=new boolean[processes.size()]; // Tracks which processes are completed initially all processes hasn't completed (false)
        int served=1; // Execution order counter
        // Continue until all processes are completed (execute all processes)
        while(complete_processes<processes.size()){
            // updated every iteration
            List <Process> readyQueue=new ArrayList<>(); // List of processes ready to execute
            // Add all processes that have arrived and are not yet finished to the ready queue
            for(Process p:processes)
                if(p.arrival<=current && !finished[p.pid-1])
                    readyQueue.add(p);
            // If no processes are ready, increment the current time and continue
            if(readyQueue.isEmpty()){
                current++;
                continue;
            }
            // Select the process with the shortest burst time from the ready queue
            Process current_process= Collections.min(readyQueue, Comparator.comparingInt(p -> p.burst));
            // print when time begin to execute
            System.out.println("Process "+current_process.pid+" is executed after waiting "+ (current-current_process.arrival)+" time from the moment its arrived");
            current+=current_process.remaining; // increment the current time by the burst time
            current_process.end=current; // Set the end time for the process (completion time)
            current_process.executed=served; // Record the execution order
            current_process.turnaround=current_process.end- current_process.arrival; // Calculate turnaround time (complete time - arrival time)
            current_process.waiting=current_process.turnaround-current_process.burst; // Calculate waiting time (turnaround time - burst time)
            finished[current_process.pid-1]=true; // Mark the process as finished (update process's state)
            complete_processes++; // Increment the count of completed processes
            served++; // Increment the execution order
            // print when finished execution 
            System.out.println("Process "+current_process.pid+" completed at "+current+" time");
            System.out.println("________________________________________________________________________");
        }
        // Sort processes by their original PID for final output
        processes.sort(Comparator.comparing(p->p.pid));
        print(); // Print the scheduling results
    }
}
